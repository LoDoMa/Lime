package net.lodoma.lime.script.library;

import net.lodoma.lime.Lime;
import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.util.Color;
import net.lodoma.lime.world.World;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class LightFunctions
{
    public static synchronized void addToLibrary(LimeLibrary library)
    {
        new LightFunctions(library);
    }
    
    public LimeLibrary library;
    public World world;
    
    private LightFunctions(LimeLibrary library)
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
            case NEW_LIGHT:
            {
                Light light = new Light(world);
                int lightID = world.lightPool.add(light);
                Lime.LOGGER.I("Created light " + lightID);
                return LuaValue.valueOf(lightID);
            }
            case REMOVE_LIGHT:
            {
                int lightID = args.arg(1).checkint();
                world.lightPool.remove(lightID);
                break;
            }
            case SET_POSITION:
            {
                int lightID = args.arg(1).checkint();
                float positionX = args.arg(2).checknumber().tofloat();
                float positionY = args.arg(3).checknumber().tofloat();
                world.lightPool.get(lightID).data.position.set(positionX, positionY);
                break;
            }
            case SET_RADIUS:
            {
                int lightID = args.arg(1).checkint();
                float radius = args.arg(2).checknumber().tofloat();
                world.lightPool.get(lightID).data.radius = radius;
                break;
            }
            case SET_COLOR:
            {
                int lightID = args.arg(1).checkint();
                float colorR = args.arg(2).checknumber().tofloat();
                float colorG = args.arg(3).checknumber().tofloat();
                float colorB = args.arg(4).checknumber().tofloat();
                float colorA = args.arg(5).checknumber().tofloat();
                Color color = world.lightPool.get(lightID).data.color;
                color.r = colorR;
                color.g = colorG;
                color.b = colorB;
                color.a = colorA;
                break;
            }
            case SET_AMBIENT_LIGHT:
            {
                float ambientR = args.arg(1).checknumber().tofloat();
                float ambientG = args.arg(2).checknumber().tofloat();
                float ambientB = args.arg(3).checknumber().tofloat();
                world.lightAmbientColor.r = ambientR;
                world.lightAmbientColor.g = ambientG;
                world.lightAmbientColor.b = ambientB;
                world.lightAmbientColor.a = 1.0f;
                break;
            }
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        NEW_LIGHT(0, true, "newLight"),
        REMOVE_LIGHT(1, true, "removeLight"),
        SET_POSITION(3, true, "setLightPosition"),
        SET_RADIUS(2, true, "setLightRadius"),
        SET_COLOR(5, true, "setLightColor"),
        SET_AMBIENT_LIGHT(3, true, "setAmbientLight");
        
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
