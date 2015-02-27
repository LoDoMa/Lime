package net.lodoma.lime.script.library;

import net.lodoma.lime.world.physics.InvalidPhysicsParticleException;
import net.lodoma.lime.world.physics.PhysicsParticleDefinition;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class ParticleFunctions
{
    public static synchronized void addToLibrary(LimeLibrary library)
    {
        new ParticleFunctions(library);
    }
    
    public LimeLibrary library;

    private PhysicsParticleDefinition particleDefinition;
    
    private ParticleFunctions(LimeLibrary library)
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
            case START_PARTICLE:
            {
                particleDefinition = new PhysicsParticleDefinition();
                break;
            }
            case END_PARTICLE:
            {
                if (particleDefinition == null)
                    throw new LuaError("ending nonexistent particle");
                
                try
                {
                    particleDefinition.validate();
                }
                catch (InvalidPhysicsParticleException e)
                {
                    throw new LuaError(e);
                }
                
                library.server.world.particleDefinitionList.add(particleDefinition);
                particleDefinition = null;
                break;
            }
            case SET_PARTICLE_POSITION:
            {
                float positionX = args.arg(1).checknumber().tofloat();
                float positionY = args.arg(2).checknumber().tofloat();
                if (particleDefinition == null)
                    throw new LuaError("modifying nonexistent particle");
                particleDefinition.position.set(positionX, positionY);
                break;
            }
            case SET_PARTICLE_ANGLE:
            {
                float angle = args.arg(1).checknumber().tofloat();
                if (particleDefinition == null)
                    throw new LuaError("modifying nonexistent particle");
                particleDefinition.angle = angle;
                break;
            }
            case SET_PARTICLE_ANGULAR_VELOCITY:
            {
                float velocity = args.arg(1).checknumber().tofloat();
                if (particleDefinition == null)
                    throw new LuaError("modifying nonexistent particle");
                particleDefinition.angularVelocity = velocity;
                break;
            }
            case SET_PARTICLE_LINEAR_VELOCITY:
            {
                float velocityX = args.arg(1).checknumber().tofloat();
                float velocityY = args.arg(2).checknumber().tofloat();
                if (particleDefinition == null)
                    throw new LuaError("modifying nonexistent particle");
                particleDefinition.linearVelocity.set(velocityX, velocityY);
                break;
            }
            case SET_PARTICLE_SIZE:
            {
                float size = args.arg(1).checknumber().tofloat();
                if (particleDefinition == null)
                    throw new LuaError("modifying nonexistent particle");
                particleDefinition.size = size;
                break;
            }
            case SET_PARTICLE_DENSITY:
            {
                float density = args.arg(1).checknumber().tofloat();
                if (particleDefinition == null)
                    throw new LuaError("modifying nonexistent particle");
                particleDefinition.density = density;
                break;
            }
            case SET_PARTICLE_RESTITUTION:
            {
                float restitution = args.arg(1).checknumber().tofloat();
                if (particleDefinition == null)
                    throw new LuaError("modifying nonexistent particle");
                particleDefinition.restitution = restitution;
                break;
            }
            case SET_PARTICLE_ANGULAR_DAMPING:
            {
                float damping = args.arg(1).checknumber().tofloat();
                if (particleDefinition == null)
                    throw new LuaError("modifying nonexistent particle");
                particleDefinition.angularDamping = damping;
                break;
            }
            case SET_PARTICLE_LINEAR_DAMPING:
            {
                float damping = args.arg(1).checknumber().tofloat();
                if (particleDefinition == null)
                    throw new LuaError("modifying nonexistent particle");
                particleDefinition.linearDamping = damping;
                break;
            }
            case SET_PARTICLE_LIFETIME:
            {
                float lifetime = args.arg(1).checknumber().tofloat();
                if (particleDefinition == null)
                    throw new LuaError("modifying nonexistent particle");
                particleDefinition.lifetime = lifetime;
                break;
            }
            case ENABLE_PARTICLE_DESPAWN_ON_COLLISION:
            {
                boolean enabled = args.arg(1).checkboolean();
                if (particleDefinition == null)
                    throw new LuaError("modifying nonexistent particle");
                particleDefinition.destroyOnCollision = enabled;
                break;
            }
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        START_PARTICLE(0, true, "startParticle"),
        END_PARTICLE(0, true, "endParticle"),
        SET_PARTICLE_POSITION(2, true, "setParticlePosition"),
        SET_PARTICLE_ANGLE(1, true, "setParticleAngle"),
        SET_PARTICLE_ANGULAR_VELOCITY(1, true, "setParticleAngularVelocity"),
        SET_PARTICLE_LINEAR_VELOCITY(2, true, "setParticleLinearVelocity"),
        SET_PARTICLE_SIZE(1, true, "setParticleSize"),
        SET_PARTICLE_DENSITY(1, true, "setParticleDensity"),
        SET_PARTICLE_RESTITUTION(1, true, "setParticleRestitution"),
        SET_PARTICLE_ANGULAR_DAMPING(1, true, "setParticleAngularDamping"),
        SET_PARTICLE_LINEAR_DAMPING(1, true, "setParticleLinearDamping"),
        SET_PARTICLE_LIFETIME(1, true, "setParticleLifetime"),
        ENABLE_PARTICLE_DESPAWN_ON_COLLISION(1, true, "enableParticleDespawnOnCollision");
        
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
