package net.lime.moduletest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import net.lime.moduletest.module.Module;

public class ModuleLoader
{
    @SuppressWarnings("unchecked")
    private static Module loadModule(File moduleFile)
    {
        try
        {
            String className = null;
            
            BufferedReader reader = new BufferedReader(new FileReader(moduleFile));
            className = reader.readLine();
            reader.close();
            
            Class<?> clazz = Class.forName(className);
            if(clazz.getSuperclass() != Module.class)
            {
                System.err.println("invalid module");
                return null;
            }
            Class<Module> module = (Class<Module>) clazz;
            Constructor<Module>[] constructors = (Constructor<Module>[]) module.getConstructors();
            Module instance = null;
            for(Constructor<Module> constructor : constructors)
                if(constructor.getParameters().length == 0)
                {
                    instance = constructor.newInstance();
                    break;
                }
            if(instance == null)
            {
                System.err.println("invalid module");
            }
            return instance;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static List<Module> loadModules()
    {
        List<Module> modules = new ArrayList<Module>();
        File[] files = new File("./modules/").listFiles();
        for(File file : files)
            if(file.getName().endsWith(".module"))
            {
                Module module = loadModule(file);
                if(module != null) modules.add(module);
            }
        return modules;
    }
}
