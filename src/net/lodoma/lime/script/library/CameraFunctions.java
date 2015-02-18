package net.lodoma.lime.script.library;

import net.lodoma.lime.server.UserManager;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class CameraFunctions
{
    public static synchronized void addToLibrary(LimeLibrary library)
    {
        new CameraFunctions(library);
    }
    
    public LimeLibrary library;
    public UserManager userManager;
    
    private CameraFunctions(LimeLibrary library)
    {
        this.library = library;
        this.userManager = library.server.userManager;
        
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
            case SET_CAMERA_TRANSLATION:
            {
                int userID = args.arg(1).checkint();
                float translationX = args.arg(2).checknumber().tofloat();
                float translationY = args.arg(3).checknumber().tofloat();
                userManager.getUser(userID).camera.translation.set(translationX, translationY);
                break;
            }
            case SET_CAMERA_ROTATION:
            {
                int userID = args.arg(1).checkint();
                float rotation = args.arg(2).checknumber().tofloat();
                userManager.getUser(userID).camera.rotation = rotation;
                break;
            }
            case SET_CAMERA_SCALE:
            {
                int userID = args.arg(1).checkint();
                float scaleX = args.arg(2).checknumber().tofloat();
                float scaleY = args.arg(3).checknumber().tofloat();
                userManager.getUser(userID).camera.scale.set(scaleX, scaleY);
                break;
            }
            }
            return LuaValue.NONE;
        }
    }
    
    private static enum FuncData
    {
        SET_CAMERA_TRANSLATION(3, true, "setCameraTranslation"),
        SET_CAMERA_ROTATION(2, true, "setCameraRotation"),
        SET_CAMERA_SCALE(3, true, "setCameraScale");
        
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
