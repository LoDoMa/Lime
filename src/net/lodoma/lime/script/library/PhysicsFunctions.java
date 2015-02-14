package net.lodoma.lime.script.library;

import net.lodoma.lime.Lime;
import net.lodoma.lime.script.LuaContactListener;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.physics.InvalidPhysicsComponentException;
import net.lodoma.lime.world.physics.InvalidPhysicsJointException;
import net.lodoma.lime.world.physics.PhysicsComponent;
import net.lodoma.lime.world.physics.PhysicsComponentCircleShape;
import net.lodoma.lime.world.physics.PhysicsComponentPolygonShape;
import net.lodoma.lime.world.physics.PhysicsComponentDefinition;
import net.lodoma.lime.world.physics.PhysicsComponentShape;
import net.lodoma.lime.world.physics.PhysicsComponentType;
import net.lodoma.lime.world.physics.PhysicsJoint;
import net.lodoma.lime.world.physics.PhysicsJointDefinition;
import net.lodoma.lime.world.physics.PhysicsJointRevoluteDefinition;
import net.lodoma.lime.world.physics.PhysicsJointType;
import net.lodoma.lime.world.physics.PhysicsJointTypeDefinition;

import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class PhysicsFunctions
{
    public static synchronized void addToLibrary(LimeLibrary library)
    {
        new PhysicsFunctions(library);
    }
    
    public LimeLibrary library;

    private PhysicsComponent compo;
    private PhysicsComponentDefinition compoDefinition;
    private PhysicsJoint joint;
    private PhysicsJointDefinition jointDefinition;
    
    private PhysicsFunctions(LimeLibrary library)
    {
        this.library = library;
        
        for (FuncData func : FuncData.values())
            new LimeFunc(func).addToLibrary();
    }
    
    private class LimeFunc extends VarArgFunction
    {
        private FuncData data;
        
        public LimeFunc(FuncData data)
        {
            this.data = data;
        }
        
        public void addToLibrary()
        {
            library.table.set(data.name, this);
        }
        
        @Override
        public Varargs invoke(Varargs args)
        {
            if ((data.argcexact && args.narg() != data.argc) || (!data.argcexact && args.narg() < data.argc))
                throw new LuaError("invalid argument count to Lime function \"" + data.name + "\"");
            
            switch (data)
            {
            case START_COMPONENT:
            {
                compoDefinition = new PhysicsComponentDefinition();
                break;
            }
            case END_COMPONENT:
            {
                if (compoDefinition == null)
                    throw new LuaError("ending nonexistent body component");
                
                try
                {
                    compoDefinition.validate();
                }
                catch (InvalidPhysicsComponentException e)
                {
                    throw new LuaError(e);
                }
                
                compoDefinition.create();
                PhysicsComponent newCompo = new PhysicsComponent(compoDefinition, library.server.physicsWorld);
                compoDefinition = null;
                
                int compoID = library.server.world.componentPool.add(newCompo);
                Lime.LOGGER.I("Created physics component " + compoID);
                return LuaValue.valueOf(compoID);
            }
            case REMOVE_COMPONENT:
            {
                int compoID = args.arg(1).checkint();
                library.server.world.componentPool.get(compoID).destroy();
                library.server.world.componentPool.remove(compoID);
                Lime.LOGGER.I("Removed physics component " + compoID);
                break;
            }
            case SET_INITIAL_POSITION:
            {
                float positionX = args.arg(1).checknumber().tofloat();
                float positionY = args.arg(2).checknumber().tofloat();
                if (compoDefinition == null)
                    throw new LuaError("modifying nonexistent body component");
                compoDefinition.position.set(positionX, positionY);
                break;
            }
            case SET_INITIAL_ANGLE:
            {
                float angle = args.arg(1).checknumber().tofloat();
                if (compoDefinition == null)
                    throw new LuaError("modifying nonexistent body component");
                compoDefinition.angle = angle;
                break;
            }
            case SET_COMPONENT_TYPE:
            {
                String typeName = args.arg(1).checkstring().tojstring();
                if (compoDefinition == null)
                    throw new LuaError("modifying nonexistent body component");
                
                switch (typeName)
                {
                case "dynamic": 
                    compoDefinition.type = PhysicsComponentType.DYNAMIC;
                    break;
                case "kinematic": 
                    compoDefinition.type = PhysicsComponentType.KINEMATIC;
                    break;
                case "static": 
                    compoDefinition.type = PhysicsComponentType.STATIC;
                    break;
                default:
                    throw new LuaError("invalid physics component typename");
                }
                break;
            }
            case SET_SHAPE_TYPE:
            {
                String typeName = args.arg(1).checkstring().tojstring();
                if (compoDefinition == null)
                    throw new LuaError("modifying nonexistent body component");
                
                PhysicsComponentShape shape = null;
                switch (typeName)
                {
                case "circle":
                    shape = new PhysicsComponentCircleShape();
                    break;
                case "polygon":
                    shape = new PhysicsComponentPolygonShape();
                    break;
                default:
                    throw new LuaError("invalid physics component shape typename");
                }
                compoDefinition.shape = shape;
                break;
            }
            case SET_SHAPE_RADIUS:
            {
                float radius = args.arg(1).checknumber().tofloat();
                if (compoDefinition == null)
                    throw new LuaError("modifying nonexistent body component");

                if (compoDefinition.shape == null)
                    throw new LuaError("setting radius to nonexistent shape");
                if (!(compoDefinition.shape instanceof PhysicsComponentCircleShape))
                    throw new LuaError("setting radius to non-circular shape");
                ((PhysicsComponentCircleShape) compoDefinition.shape).radius = radius;
                break;
            }
            case SET_SHAPE_VERTICES:
            {
                if ((args.narg() % 2) != 0)
                    throw new LuaError("argument count to \"" + data.name + "\" must be even");
                if (args.narg() < 6)
                    throw new LuaError("insufficient arguments to \"" + data.name + "\", minimum of 6 required");
                if (args.narg() > Settings.maxPolygonVertices * 2)
                    throw new LuaError("too many arguments to \"" + data.name + "\", maximum vertex count " + Settings.maxPolygonVertices);

                if (compoDefinition == null)
                    throw new LuaError("modifying nonexistent body component");
                if (compoDefinition.shape == null)
                    throw new LuaError("setting vertices to nonexistent shape");
                if (!(compoDefinition.shape instanceof PhysicsComponentPolygonShape))
                    throw new LuaError("setting vertices to non-polygonal shape");
                
                PhysicsComponentPolygonShape shape = (PhysicsComponentPolygonShape) compoDefinition.shape;
                shape.vertices = new Vector2[args.narg() / 2];
                for (int i = 0; i < args.narg() / 2; i++)
                    shape.vertices[i] = new Vector2(args.arg(i * 2 + 1).checknumber().tofloat(), args.arg(i * 2 + 2).checknumber().tofloat());
                break;
            }
            case SET_COMPONENT_DENSITY:
            {
                float density = args.arg(1).checknumber().tofloat();
                if (compoDefinition == null)
                    throw new LuaError("modifying nonexistent body component");
                
                compoDefinition.density = density;
                break;
            }
            case SET_COMPONENT_FRICTION:
            {
                float friction = args.arg(1).checknumber().tofloat();
                if (compoDefinition == null)
                    throw new LuaError("modifying nonexistent body component");
                
                compoDefinition.friction = friction;
                break;
            }
            case SET_COMPONENT_RESTITUTION:
            {
                float restitution = args.arg(1).checknumber().tofloat();
                if (compoDefinition == null)
                    throw new LuaError("modifying nonexistent body component");
                
                compoDefinition.restitution = restitution;
                break;
            }
            case START_JOINT:
            {
                jointDefinition = new PhysicsJointDefinition();
                break;
            }
            case END_JOINT:
            {
                if (jointDefinition == null)
                    throw new LuaError("ending nonexistent joint");
                
                try
                 {
                    jointDefinition.validate();
                }
                catch (InvalidPhysicsJointException e)
                {
                    throw new LuaError(e);
                }
                
                jointDefinition.create();
                PhysicsJoint newJoint = new PhysicsJoint(jointDefinition, library.server.physicsWorld);
                jointDefinition = null;
                
                int jointID = library.server.world.jointPool.add(newJoint);
                Lime.LOGGER.I("Created physics joint " + jointID);
                return LuaValue.valueOf(jointID);
            }
            case REMOVE_JOINT:
            {
                int jointID = args.arg(1).checkint();
                library.server.world.jointPool.get(jointID).destroy();
                library.server.world.jointPool.remove(jointID);
                Lime.LOGGER.I("Removed physics joint " + jointID);
                break;
            }
            case SET_JOINT_COMPONENT_A:
            {
                int compoID = args.arg(1).checkint();
                if (jointDefinition == null)
                    throw new LuaError("modifying nonexistent joint");
                
                if (!library.server.world.componentPool.has(compoID))
                    throw new LuaError("joint component A set to nonexistent component");
                jointDefinition.componentA = library.server.world.componentPool.get(compoID);
                break;
            }
            case SET_JOINT_COMPONENT_B:
            {
                int compoID = args.arg(1).checkint();
                if (jointDefinition == null)
                    throw new LuaError("modifying nonexistent joint");
                
                if (!library.server.world.componentPool.has(compoID))
                    throw new LuaError("joint component B set to nonexistent component");
                jointDefinition.componentB = library.server.world.componentPool.get(compoID);
                break;
            }
            case ENABLE_JOINT_COLLISION:
            {
                boolean enabled = args.arg(1).checkboolean();
                if (jointDefinition == null)
                    throw new LuaError("modifying nonexistent joint");
                
                jointDefinition.collideConnected = enabled;
                break;
            }
            case SET_JOINT_TYPE:
            {
                String typeName = args.arg(1).checkstring().tojstring();
                if (jointDefinition == null)
                    throw new LuaError("modifying nonexistent joint");

                PhysicsJointType type;
                PhysicsJointTypeDefinition typedef;
                
                switch (typeName)
                {
                case "revolute":
                    type = PhysicsJointType.REVOLUTE;
                    typedef = new PhysicsJointRevoluteDefinition();
                    break;
                default:
                    throw new LuaError("invalid joint typename");
                }
                
                jointDefinition.type = type;
                jointDefinition.typeDefinition = typedef;
                break;
            }
            case SET_REVOLUTE_ANCHOR_A:
            {
                float anchorX = args.arg(1).checknumber().tofloat();
                float anchorY = args.arg(2).checknumber().tofloat();
                if (jointDefinition == null)
                    throw new LuaError("modifying nonexistent joint");

                if (jointDefinition.type == null)
                    throw new LuaError("setting anchor to typeless joint");
                if (!(jointDefinition.typeDefinition instanceof PhysicsJointRevoluteDefinition))
                    throw new LuaError("setting anchor to non-revolute joint");
                ((PhysicsJointRevoluteDefinition) jointDefinition.typeDefinition).localAnchorA = new Vector2(anchorX, anchorY);
                break;
            }
            case SET_REVOLUTE_ANCHOR_B:
            {   
                float anchorX = args.arg(1).checknumber().tofloat();
                float anchorY = args.arg(2).checknumber().tofloat();
                if (jointDefinition == null)
                    throw new LuaError("modifying nonexistent joint");

                if (jointDefinition.type == null)
                    throw new LuaError("setting anchor to typeless joint");
                if (!(jointDefinition.typeDefinition instanceof PhysicsJointRevoluteDefinition))
                    throw new LuaError("setting anchor to non-revolute joint");
                ((PhysicsJointRevoluteDefinition) jointDefinition.typeDefinition).localAnchorB = new Vector2(anchorX, anchorY);
                break;
            }
            case SELECT_COMPONENT:
            {
                int compoID = args.arg(1).checkint();
                compo = library.server.world.componentPool.get(compoID);
                break;
            }
            case GET_POSITION:
            {
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                Vec2 position = compo.engineBody.getPosition();
                return LuaValue.varargsOf(new LuaValue[] { LuaValue.valueOf(position.x), LuaValue.valueOf(position.y) });
            }
            case GET_ANGLE:
            {
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                float angle = compo.engineBody.getAngle();
                return LuaValue.valueOf(angle);
            }
            case GET_LINEAR_VELOCITY:
            {
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                Vec2 velocity = compo.engineBody.getLinearVelocity();
                return LuaValue.varargsOf(new LuaValue[] { LuaValue.valueOf(velocity.x), LuaValue.valueOf(velocity.y) });
            }
            case SET_LINEAR_VELOCITY:
            {
                float velocityX = args.arg(1).checknumber().tofloat();
                float velocityY = args.arg(2).checknumber().tofloat();
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                compo.engineBody.setLinearVelocity(new Vec2(velocityX, velocityY));
                break;
            }
            case GET_ANGULAR_VELOCITY:
            {
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                float velocity = compo.engineBody.getAngularVelocity();
                return LuaValue.valueOf(velocity);
            }
            case SET_ANGULAR_VELOCITY:
            {
                float velocity = args.arg(1).checknumber().tofloat();
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                compo.engineBody.setAngularVelocity(velocity);
                break;
            }
            case GET_LINEAR_DAMPING:
            {
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                float damping = compo.engineBody.getLinearDamping();
                return LuaValue.valueOf(damping);
            }
            case SET_LINEAR_DAMPING:
            {
                float damping = args.arg(1).checknumber().tofloat();
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                compo.engineBody.setLinearDamping(damping);
                break;
            }
            case GET_ANGULAR_DAMPING:
            {
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                float damping = compo.engineBody.getAngularDamping();
                return LuaValue.valueOf(damping);
            }
            case SET_ANGULAR_DAMPING:
            {
                float damping = args.arg(1).checknumber().tofloat();
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                compo.engineBody.setAngularDamping(damping);
                break;
            }
            case GET_ANGLE_LOCKED:
            {
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                boolean locked = compo.engineBody.isFixedRotation();
                return LuaValue.valueOf(locked);
            }
            case SET_ANGLE_LOCKED:
            {
                boolean locked = args.arg(1).checkboolean();
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                compo.engineBody.setFixedRotation(locked);
                break;
            }
            case APPLY_FORCE:
            {
                float forceX = args.arg(1).checknumber().tofloat();
                float forceY = args.arg(2).checknumber().tofloat();
                float pointX = args.arg(3).checknumber().tofloat();
                float pointY = args.arg(4).checknumber().tofloat();
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                compo.engineBody.applyForce(new Vec2(forceX, forceY), new Vec2(pointX, pointY));
                break;
            }
            case APPLY_FORCE_TO_CENTER:
            {
                float forceX = args.arg(1).checknumber().tofloat();
                float forceY = args.arg(2).checknumber().tofloat();
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                compo.engineBody.applyForceToCenter(new Vec2(forceX, forceY));
                break;
            }
            case APPLY_ANGULAR_IMPULSE:
            {
                float impulse = args.arg(1).checknumber().tofloat();
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                compo.engineBody.applyAngularImpulse(impulse);
                break;
            }
            case APPLY_LINEAR_IMPULSE:
            {
                float impulseX = args.arg(1).checknumber().tofloat();
                float impulseY = args.arg(2).checknumber().tofloat();
                float pointX = args.arg(3).checknumber().tofloat();
                float pointY = args.arg(4).checknumber().tofloat();
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                compo.engineBody.applyLinearImpulse(new Vec2(impulseX, impulseY), new Vec2(pointX, pointY));
                break;
            }
            case APPLY_LINEAR_IMPULSE_TO_CENTER:
            {
                float impulseX = args.arg(1).checknumber().tofloat();
                float impulseY = args.arg(2).checknumber().tofloat();
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                compo.engineBody.applyLinearImpulse(new Vec2(impulseX, impulseY), compo.engineBody.getLocalCenter());
                break;
            }
            case GET_USING_CCD:
            {
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                boolean using = compo.engineBody.isBullet();
                return LuaValue.valueOf(using);
            }
            case SET_USING_CCD:
            {
                boolean using = args.arg(1).checkboolean();
                if (compo == null)
                    throw new LuaError("manipulating nonexistent body component");
                compo.engineBody.setBullet(using);
                break;
            }
            case SELECT_JOINT:
            {
                int jointID = args.arg(1).checkint();
                joint = library.server.world.jointPool.get(jointID);
                break;
            }
            case ENABLE_REVOLUTE_ANGLE_LIMIT:
            {
                boolean enabled = args.arg(1).checkboolean();
                if (joint == null)
                    throw new LuaError("modifying nonexistent joint");

                if (!(joint.engineJoint instanceof RevoluteJoint))
                    throw new LuaError("modifying angle limit of non-revolute joint");
                ((RevoluteJoint) joint.engineJoint).enableLimit(enabled);
                break;
            }
            case SET_REVOLUTE_ANGLE_LIMIT:
            {
                float lowerLimit = args.arg(1).checknumber().tofloat();
                float upperLimit = args.arg(2).checknumber().tofloat();
                if (joint == null)
                    throw new LuaError("modifying nonexistent joint");

                if (!(joint.engineJoint instanceof RevoluteJoint))
                    throw new LuaError("modifying angle limit of non-revolute joint");
                ((RevoluteJoint) joint.engineJoint).setLimits(lowerLimit, upperLimit);
                break;
            }
            case ENABLE_REVOLUTE_MOTOR:
            {
                boolean enabled = args.arg(1).checkboolean();
                if (joint == null)
                    throw new LuaError("modifying nonexistent joint");

                if (!(joint.engineJoint instanceof RevoluteJoint))
                    throw new LuaError("modifying angle limit of non-revolute joint");
                ((RevoluteJoint) joint.engineJoint).enableMotor(enabled);
                break;
            }
            case SET_REVOLUTE_MOTOR_SPEED:
            {
                float speed = args.arg(1).checknumber().tofloat();
                if (joint == null)
                    throw new LuaError("modifying nonexistent joint");

                if (!(joint.engineJoint instanceof RevoluteJoint))
                    throw new LuaError("modifying angle limit of non-revolute joint");
                ((RevoluteJoint) joint.engineJoint).setMotorSpeed(speed);
                break;
            }
            case SET_REVOLUTE_MAX_MOTOR_TORQUE:
            {
                float maxTorque = args.arg(1).checknumber().tofloat();
                if (joint == null)
                    throw new LuaError("modifying nonexistent joint");

                if (!(joint.engineJoint instanceof RevoluteJoint))
                    throw new LuaError("modifying angle limit of non-revolute joint");
                ((RevoluteJoint) joint.engineJoint).setMaxMotorTorque(maxTorque);
                break;
            }
            case ADD_CONTACT_LISTENER:
            {
                if (args.narg() > 4)
                    throw new LuaError("too many arguments to \"" + data.name + "\"");
                LuaFunction preSolve = args.arg(1).checkfunction();
                LuaFunction postSolve = args.arg(2).checkfunction();
                
                int filterLevel = 0;
                int bodyA = 0;
                int bodyB = 0;
                if (args.narg() > 3)
                {
                    filterLevel++;
                    bodyA = args.arg(3).checkint();
                }
                if (args.narg() == 4)
                {
                    filterLevel++;
                    bodyB = args.arg(4).checkint();
                }
                
                LuaContactListener listener = null;
                if (filterLevel == 0) listener = new LuaContactListener(preSolve, postSolve);
                else if (filterLevel == 1) listener = new LuaContactListener(bodyA, preSolve, postSolve);
                else if (filterLevel == 2) listener = new LuaContactListener(bodyA, bodyB, preSolve, postSolve);
                
                int listenerID = library.server.physicsWorld.contactManager.listeners.add(listener);
                Lime.LOGGER.I("Added contact listener " + listenerID + ", listening to " +
                        ((filterLevel == 0) ? "everything" : ((filterLevel == 1) ? ("collisions with " + bodyA) : ("collisions between " + bodyA + " and " + bodyB))));
                return LuaValue.valueOf(listenerID);
            }
            case REMOVE_CONTACT_LISTENER:
            {
                int listenerID = args.arg(1).checkint();
                library.server.physicsWorld.contactManager.listeners.remove(listenerID);
                break;
            }
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        START_COMPONENT(0, true, "startComponent"),
        END_COMPONENT(0, true, "endComponent"),
        REMOVE_COMPONENT(1, true, "removeComponent"),
        SET_INITIAL_POSITION(2, true, "setInitialPosition"),
        SET_INITIAL_ANGLE(1, true, "setInitialAngle"),
        SET_COMPONENT_TYPE(1, true, "setComponentType"),
        SET_SHAPE_TYPE(1, true, "setShapeType"),
        SET_SHAPE_RADIUS(1, true, "setShapeRadius"),
        SET_SHAPE_VERTICES(0, false, "setShapeVertices"),
        SET_COMPONENT_DENSITY(1, true, "setComponentDensity"),
        SET_COMPONENT_FRICTION(1, true, "setComponentFriction"),
        SET_COMPONENT_RESTITUTION(1, true, "setComponentRestitution"),
        
        START_JOINT(0, true, "startJoint"),
        END_JOINT(0, true, "endJoint"),
        REMOVE_JOINT(1, true, "removeJoint"),
        SET_JOINT_COMPONENT_A(1, true, "setJointComponentA"),
        SET_JOINT_COMPONENT_B(1, true, "setJointComponentB"),
        ENABLE_JOINT_COLLISION(1, true, "enableJointCollision"),
        SET_JOINT_TYPE(1, true, "setJointType"),
        SET_REVOLUTE_ANCHOR_A(2, true, "setRevoluteAnchorA"),
        SET_REVOLUTE_ANCHOR_B(2, true, "setRevoluteAnchorB"),

        SELECT_COMPONENT(1, true, "selectComponent"),
        GET_POSITION(0, true, "getComponentPosition"),
        GET_ANGLE(0, true, "getComponentAngle"),
        GET_LINEAR_VELOCITY(0, true, "getLinearVelocity"),
        SET_LINEAR_VELOCITY(2, true, "setLinearVelocity"),
        GET_ANGULAR_VELOCITY(0, true, "getAngularVelocity"),
        SET_ANGULAR_VELOCITY(1, true, "setAngularVelocity"),
        GET_LINEAR_DAMPING(0, true, "getLinearDamping"),
        SET_LINEAR_DAMPING(1, true, "setLinearDamping"),
        GET_ANGULAR_DAMPING(0, true, "getAngularDamping"),
        SET_ANGULAR_DAMPING(1, true, "setAngularDamping"),
        GET_ANGLE_LOCKED(0, true, "getAngleLocked"),
        SET_ANGLE_LOCKED(1, true, "setAngleLocked"),
        APPLY_FORCE(4, true, "applyForce"),
        APPLY_FORCE_TO_CENTER(2, true, "applyForceToCenter"),
        APPLY_ANGULAR_IMPULSE(1, true, "applyAngularImpulse"),
        APPLY_LINEAR_IMPULSE(4, true, "applyLinearImpulse"),
        APPLY_LINEAR_IMPULSE_TO_CENTER(2, true, "applyLinearImpulseToCenter"),
        GET_USING_CCD(0, true, "getUsingCCD"),
        SET_USING_CCD(1, true, "setUsingCCD"),
        
        SELECT_JOINT(1, true, "selectJoint"),
        ENABLE_REVOLUTE_ANGLE_LIMIT(1, true, "enableRevoluteAngleLimit"),
        SET_REVOLUTE_ANGLE_LIMIT(1, true, "setRevoluteAngleLimit"),
        ENABLE_REVOLUTE_MOTOR(1, true, "enableRevoluteMotor"),
        SET_REVOLUTE_MOTOR_SPEED(1, true, "setRevoluteMotorSpeed"),
        SET_REVOLUTE_MAX_MOTOR_TORQUE(1, true, "setRevoluteMaxMotorTorque"),
        
        ADD_CONTACT_LISTENER(2, false, "addContactListener"),
        REMOVE_CONTACT_LISTENER(1, true, "removeContactListener");
        
        public int argc;
        public boolean argcexact;
        public String name;
        
        private FuncData(int argc, boolean argcexact, String name)
        {
            this.argc = argc;
            this.argcexact = argcexact;
            this.name = name;
        }
    }
}
