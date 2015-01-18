package net.lodoma.lime.script.library;

import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.physics.InvalidPhysicsComponentException;
import net.lodoma.lime.world.physics.PhysicsComponent;
import net.lodoma.lime.world.physics.PhysicsComponentCircleShape;
import net.lodoma.lime.world.physics.PhysicsComponentPolygonShape;
import net.lodoma.lime.world.physics.PhysicsComponentDefinition;
import net.lodoma.lime.world.physics.PhysicsComponentShape;
import net.lodoma.lime.world.physics.PhysicsComponentType;

import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;
import org.luaj.vm2.LuaError;
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
            case ATTACH_COMPONENT_TO_ENTITY:
            {
                int entityID = args.arg(1).checkint();
                if (compoDefinition == null)
                    throw new LuaError("attaching nonexistent body component");
                
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
                
                int compoID = library.server.world.entityPool.get(entityID).body.components.add(newCompo);
                return LuaValue.valueOf(compoID);
            }
            case ATTACH_COMPONENT_TO_TERRAIN:
            {
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
                
                int compoID = library.server.world.terrain.physicalComponents.add(newCompo);
                return LuaValue.valueOf(compoID);
            }
            case SELECT_ENTITY_COMPONENT:
            {
                int entityID = args.arg(1).checkint();
                int compoID = args.arg(2).checkint();
                compo = library.server.world.entityPool.get(entityID).body.components.get(compoID);
                break;
            }
            case SELECT_TERRAIN_COMPONENT:
            {
                int compoID = args.arg(1).checkint();
                compo = library.server.world.terrain.physicalComponents.get(compoID);
                break;
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
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        START_COMPONENT(0, true, "startComponent"),
        SET_INITIAL_POSITION(2, true, "setInitialPosition"),
        SET_INITIAL_ANGLE(1, true, "setInitialAngle"),
        SET_COMPONENT_TYPE(1, true, "setComponentType"),
        SET_SHAPE_TYPE(1, true, "setShapeType"),
        SET_SHAPE_RADIUS(1, true, "setShapeRadius"),
        SET_SHAPE_VERTICES(0, false, "setShapeVertices"),
        SET_COMPONENT_DENSITY(1, true, "setComponentDensity"),
        SET_COMPONENT_FRICTION(1, true, "setComponentFriction"),
        SET_COMPONENT_RESTITUTION(1, true, "setComponentRestitution"),
        ATTACH_COMPONENT_TO_ENTITY(1, true, "attachComponentToEntity"),
        ATTACH_COMPONENT_TO_TERRAIN(0, true, "attachComponentToTerrain"),

        SELECT_ENTITY_COMPONENT(2, true, "selectEntityComponent"),
        SELECT_TERRAIN_COMPONENT(1, true, "selectTerrainComponent"),
        GET_LINEAR_VELOCITY(0, true, "getLinearVelocity"),
        SET_LINEAR_VELOCITY(2, true, "setLinearVelocity"),
        APPLY_FORCE(4, true, "applyForce"),
        APPLY_FORCE_TO_CENTER(2, true, "applyForceToCenter"),
        APPLY_ANGULAR_IMPULSE(1, true, "applyAngularImpulse"),
        APPLY_LINEAR_IMPULSE(4, true, "applyLinearImpulse"),
        APPLY_LINEAR_IMPULSE_TO_CENTER(2, true, "applyLinearImpulseToCenter");
        
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
