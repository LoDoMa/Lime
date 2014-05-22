package net.lime.moduletest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import net.lime.moduletest.module.Module;
import net.lime.moduletest.module.ModuleClass;

public class ModuleLoader
{
    private static ModuleClass loadModule(File moduleFile)
    {
        try
        {
            String className = null;
            
            BufferedReader reader = new BufferedReader(new FileReader(moduleFile));
            className = reader.readLine();
            reader.close();
            
            Class<?> clazz = Class.forName(className);
            Annotation[] annotations = clazz.getAnnotations();
            for(Annotation annotation : annotations)
                if(annotation instanceof Module)
                    return ModuleClass.constructModuleClass(clazz);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static List<ModuleClass> loadModules()
    {
        List<ModuleClass> modules = new ArrayList<ModuleClass>();
        File[] files = new File("./modules/").listFiles();
        for(File file : files)
            if(file.getName().endsWith(".module"))
                modules.add(loadModule(file));
        return modules;
    }
}
