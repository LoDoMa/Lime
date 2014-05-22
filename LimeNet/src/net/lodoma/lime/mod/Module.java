package net.lodoma.lime.mod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.lodoma.lime.util.AnnotationHelper;

public final class Module
{
    private Object instance;
    
    private Method preinitMethod;
    private Method initMethod;
    private Method postinitMethod;
    
    private ModTarget moduleTarget;
    private String moduleName;
    private String moduleAuthor;
    
    public Module(Class<?> moduleClass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        Mod modAnnot = (Mod) AnnotationHelper.getAnnotation(moduleClass, Mod.class);
        if (modAnnot == null)
        {
            System.err.println("missing Mod annotation");
            return;
        }

        moduleTarget = modAnnot.target();
        moduleName = modAnnot.name();
        moduleAuthor = modAnnot.author();
        
        Method[] methods = moduleClass.getMethods();
        for (Method method : methods)
            if (AnnotationHelper.isAnnotationPresent(method, ModInit.class))
                if (method.getParameterCount() == 1)
                {
                    ModInit modInitAnnot = (ModInit) AnnotationHelper.getAnnotation(method, ModInit.class);
                    InitPriority priority = modInitAnnot.priority();
                    Class<?> parameterType = null;
                    switch (priority)
                    {
                    case PREINIT:
                        if (preinitMethod != null)
                        {
                            System.err.println("duplicate preinit method");
                            return;
                        }
                        parameterType = PreinitBundle.class;
                        break;
                    case INIT:
                        if (initMethod != null)
                        {
                            System.err.println("duplicate init method");
                            return;
                        }
                        parameterType = InitBundle.class;
                        break;
                    case POSTINIT:
                        if (postinitMethod != null)
                        {
                            System.err.println("duplicate postinit method");
                            return;
                        }
                        parameterType = PostinitBundle.class;
                        break;
                    }
                    if(method.getParameterTypes()[0] != parameterType)
                    {
                        System.err.println("invalid parameter type");
                        return;
                    }
                    switch (priority)
                    {
                    case PREINIT:
                        preinitMethod = method;
                        break;
                    case INIT:
                        initMethod = method;
                        break;
                    case POSTINIT:
                        postinitMethod = method;
                        break;
                    }
                }
                else
                {
                    System.err.println("invalid parameter count");
                    return;
                }
        
        Constructor<?>[] constrs = moduleClass.getConstructors();
        for(Constructor<?> constr : constrs)
            if(constr.getParameterCount() == 0)
            {
                instance = constr.newInstance();
                break;
            }
        
        if(instance == null)
        {
            System.err.println("failed to create instance");
            return;
        }
    }
    
    public boolean hasPreinit()
    {
        return preinitMethod != null;
    }
    
    public boolean hasInit()
    {
        return initMethod != null;
    }
    
    public boolean hasPostinit()
    {
        return postinitMethod != null;
    }
    
    public void invokePreinit(PreinitBundle bundle) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        preinitMethod.invoke(instance, bundle);
    }
    
    public void invokeInit(InitBundle bundle) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        initMethod.invoke(instance, bundle);
    }
    
    public void invokePostinit(PostinitBundle bundle) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        postinitMethod.invoke(instance, bundle);
    }
}
