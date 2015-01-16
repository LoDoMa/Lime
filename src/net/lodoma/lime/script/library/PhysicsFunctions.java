package net.lodoma.lime.script.library;

import net.lodoma.lime.world.physics.InvalidPhysicsComponentException;
import net.lodoma.lime.world.physics.PhysicsComponent;
import net.lodoma.lime.world.physics.PhysicsComponentCircleShape;
import net.lodoma.lime.world.physics.PhysicsComponentDefinition;
import net.lodoma.lime.world.physics.PhysicsComponentShape;
import net.lodoma.lime.world.physics.PhysicsComponentType;

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
                compoDefinition.position.set(positionX, positionY);
                break;
            }
            case SET_INITIAL_ANGLE:
            {
                float angle = args.arg(1).checknumber().tofloat();
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
            case SET_SHAPE_DENSITY:
            {
                float density = args.arg(1).checknumber().tofloat();
                if (compoDefinition == null)
                    throw new LuaError("modifying nonexistent body component");
                
                compoDefinition.density = density;
                break;
            }
            case SET_SHAPE_FRICTION:
            {
                float friction = args.arg(1).checknumber().tofloat();
                if (compoDefinition == null)
                    throw new LuaError("modifying nonexistent body component");
                
                compoDefinition.friction = friction;
                break;
            }
            case SET_SHAPE_RESTITUTION:
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
            case SELECT_ENTITY_COMPONENT:
            {
                int entityID = args.arg(1).checkint();
                int compoID = args.arg(2).checkint();
                compo = library.server.world.entityPool.get(entityID).body.components.get(compoID);
                break;
            }
            case GET_LINEAR_VELOCITY:
            {
                Vec2 velocity = compo.engineBody.getLinearVelocity();
                return LuaValue.varargsOf(new LuaValue[] { LuaValue.valueOf(velocity.x), LuaValue.valueOf(velocity.y) });
            }
            case SET_LINEAR_VELOCITY:
            {
                float velocityX = args.arg(1).checknumber().tofloat();
                float velocityY = args.arg(2).checknumber().tofloat();
                compo.engineBody.setLinearVelocity(new Vec2(velocityX, velocityY));
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
        SET_SHAPE_DENSITY(1, true, "setShapeDensity"),
        SET_SHAPE_FRICTION(1, true, "setShapeFriction"),
        SET_SHAPE_RESTITUTION(1, true, "setShapeRestitution"),
        ATTACH_COMPONENT_TO_ENTITY(1, true, "attachComponentToEntity"),
        
        SELECT_ENTITY_COMPONENT(2, true, "selectEntityComponent"),
        GET_LINEAR_VELOCITY(0, true, "getLinearVelocity"),
        SET_LINEAR_VELOCITY(2, true, "setLinearVelocity");
        
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
