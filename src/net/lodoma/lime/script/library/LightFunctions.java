package net.lodoma.lime.script.library;

import net.lodoma.lime.shader.light.Light;
import net.lodoma.lime.shader.light.LightData;
import net.lodoma.lime.world.World;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

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
                Light light = new Light();
                int lightID = world.lightPool.add(light);
                return CoerceJavaToLua.coerce(lightID);
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
                world.lightPool.get(lightID).data.color.set(colorR, colorG, colorB, colorA);
                break;
            }
            case SET_ANGLE_RANGE:
            {
                int lightID = args.arg(1).checkint();
                float begin = args.arg(2).checknumber().tofloat();
                float end = args.arg(3).checknumber().tofloat();
                LightData data = world.lightPool.get(lightID).data;
                data.angleRangeBegin = begin;
                data.angleRangeEnd = end;
                break;
            }
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        NEW_LIGHT(0, true, "newLight"),
        SET_POSITION(3, true, "setLightPosition"),
        SET_RADIUS(2, true, "setLightRadius"),
        SET_COLOR(5, true, "setLightColor"),
        SET_ANGLE_RANGE(3, true, "setLightAngleRange");
        
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