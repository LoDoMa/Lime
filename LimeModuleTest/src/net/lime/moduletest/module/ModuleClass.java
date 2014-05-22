package net.lime.moduletest.module;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public final class ModuleClass
{
    public static ModuleClass constructModuleClass(Class<?> clazz)
    {
        Method initMethod = null;
        
        Method[] methods = clazz.getMethods();
        for(Method method : methods)
        {
            Annotation[] annotations = method.getAnnotations();
            for(Annotation annotation : annotations)
            {
                if(annotation instanceof ModuleInit)
                {
                    Class<?>[] paramaterTypes = method.getParameterTypes();
                    if(paramaterTypes.length != 1)
                        continue;
                    if(paramaterTypes[0] != InitBundle.class)
                        continue;
                    if(initMethod != null)
                        continue;
                    initMethod = method;
                }
            }
        }
        
        ModuleClass moduleClass = new ModuleClass(initMethod);
        return moduleClass;
    }
    
    private final Method INIT;
    
    public ModuleClass(Method init)
    {
        INIT = init;
    }
    
    public void runInit(InitBundle bundle)
    {
        
    }
}
