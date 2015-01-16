package net.lodoma.lime.script.library;

import net.lodoma.lime.world.World;
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

public class EntityFunctions
{
    public static synchronized void addToLibrary(LimeLibrary library)
    {
        new EntityFunctions(library);
    }
    
    public LimeLibrary library;
    public World world;
    
    private PhysicsComponentDefinition bodyCompoDefinition;
    
    private EntityFunctions(LimeLibrary library)
    {
        this.library = library;
        this.world = library.server.world;
        
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
                bodyCompoDefinition = new PhysicsComponentDefinition();
                break;
            }
            case ATTACH_COMPONENT:
            {
                int entityID = args.arg(1).checkint();
                if (bodyCompoDefinition == null)
                    throw new LuaError("attaching nonexistent body component");
                
                try
                {
                    bodyCompoDefinition.validate();
                }
                catch (InvalidPhysicsComponentException e)
                {
                    throw new LuaError(e);
                }
                
                bodyCompoDefinition.create();
                PhysicsComponent bodyCompo = new PhysicsComponent(bodyCompoDefinition, library.server.physicsWorld);
                bodyCompoDefinition = null;
                
                int compoID = world.entityPool.get(entityID).body.components.add(bodyCompo);
                return LuaValue.valueOf(compoID);
            }
            case SET_INITIAL_POSITION:
            {
                float positionX = args.arg(1).checknumber().tofloat();
                float positionY = args.arg(2).checknumber().tofloat();
                bodyCompoDefinition.position.set(positionX, positionY);
                break;
            }
            case SET_INITIAL_ANGLE:
            {
                float angle = args.arg(1).checknumber().tofloat();
                bodyCompoDefinition.angle = angle;
                break;
            }
            case SET_COMPONENT_TYPE:
            {
                String typeName = args.arg(1).checkstring().tojstring();
                if (bodyCompoDefinition == null)
                    throw new LuaError("modifying nonexistent body component");
                
                switch (typeName)
                {
                case "dynamic": 
                    bodyCompoDefinition.type = PhysicsComponentType.DYNAMIC;
                    break;
                case "kinematic": 
                    bodyCompoDefinition.type = PhysicsComponentType.KINEMATIC;
                    break;
                case "static": 
                    bodyCompoDefinition.type = PhysicsComponentType.STATIC;
                    break;
                default:
                    throw new LuaError("invalid physics component typename");
                }
                break;
            }
            case SET_SHAPE_TYPE:
            {
                String typeName = args.arg(1).checkstring().tojstring();
                if (bodyCompoDefinition == null)
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
                bodyCompoDefinition.shape = shape;
                break;
            }
            case SET_SHAPE_RADIUS:
            {
                float radius = args.arg(1).checknumber().tofloat();
                if (bodyCompoDefinition == null)
                    throw new LuaError("modifying nonexistent body component");

                if (bodyCompoDefinition.shape == null)
                    throw new LuaError("setting radius to nonexistent shape");
                if (!(bodyCompoDefinition.shape instanceof PhysicsComponentCircleShape))
                    throw new LuaError("setting radius to non-circular shape");
                ((PhysicsComponentCircleShape) bodyCompoDefinition.shape).radius = radius;
                break;
            }
            case SET_SHAPE_DENSITY:
            {
                float density = args.arg(1).checknumber().tofloat();
                if (bodyCompoDefinition == null)
                    throw new LuaError("modifying nonexistent body component");
                
                bodyCompoDefinition.density = density;
                break;
            }
            case SET_SHAPE_FRICTION:
            {
                float friction = args.arg(1).checknumber().tofloat();
                if (bodyCompoDefinition == null)
                    throw new LuaError("modifying nonexistent body component");
                
                bodyCompoDefinition.friction = friction;
                break;
            }
            case SET_SHAPE_RESTITUTION:
            {
                float restitution = args.arg(1).checknumber().tofloat();
                if (bodyCompoDefinition == null)
                    throw new LuaError("modifying nonexistent body component");
                
                bodyCompoDefinition.restitution = restitution;
                break;
            }

            case GET_LINEAR_VELOCITY:
            {
                int entityID = args.arg(1).checkint();
                int compoID = args.arg(2).checkint();
                
                Vec2 velocity = world.entityPool.get(entityID).body.components.get(compoID).engineBody.getLinearVelocity();
                return LuaValue.varargsOf(new LuaValue[] { LuaValue.valueOf(velocity.x), LuaValue.valueOf(velocity.y) });
            }
            case SET_LINEAR_VELOCITY:
            {
                int entityID = args.arg(1).checkint();
                int compoID = args.arg(2).checkint();
                float velocityX = args.arg(3).checknumber().tofloat();
                float velocityY = args.arg(4).checknumber().tofloat();
                
                world.entityPool.get(entityID).body.components.get(compoID).engineBody.setLinearVelocity(new Vec2(velocityX, velocityY));
                break;
            }
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        START_COMPONENT(0, true, "startComponent"),
        ATTACH_COMPONENT(1, true, "attachComponent"),
        SET_INITIAL_POSITION(2, true, "setInitialPosition"),
        SET_INITIAL_ANGLE(1, true, "setInitialAngle"),
        SET_COMPONENT_TYPE(1, true, "setComponentType"),
        SET_SHAPE_TYPE(1, true, "setShapeType"),
        SET_SHAPE_RADIUS(1, true, "setShapeRadius"),
        SET_SHAPE_DENSITY(1, true, "setShapeDensity"),
        SET_SHAPE_FRICTION(1, true, "setShapeFriction"),
        SET_SHAPE_RESTITUTION(1, true, "setShapeRestitution"),

        GET_LINEAR_VELOCITY(2, true, "getLinearVelocity"),
        SET_LINEAR_VELOCITY(4, true, "setLinearVelocity");
        
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
