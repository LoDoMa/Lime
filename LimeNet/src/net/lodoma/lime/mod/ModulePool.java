package net.lodoma.lime.mod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

public final class ModulePool
{
    private Set<String> moduleNames;
    private Set<Module> modules;
    
    public ModulePool()
    {
        moduleNames = new HashSet<String>();
        modules = new HashSet<Module>();
    }
    
    public void loadModules(ModTarget target) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        File[] files = new File("./mod").listFiles();
        for (File file : files)
            if (file.getName().endsWith(".mod"))
            {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String jarPath = reader.readLine();
                String className = reader.readLine();
                reader.close();
                ClassLoader classLoader;
                if (jarPath.equals("localjar"))
                    classLoader = getClass().getClassLoader();
                else
                    classLoader = URLClassLoader.newInstance(new URL[]
                    { file.toURI().toURL() }, getClass().getClassLoader());
                Class<?> moduleClass = Class.forName(className, true, classLoader);
                Module module = new Module(moduleClass);
                if(module.getModuleTarget() == target)
                {
                    String moduleName = module.getModuleName();
                    if(moduleNames.contains(moduleName))
                        System.err.println("duplicate module");
                    else
                    {
                        modules.add(module);
                        moduleNames.add(moduleName);
                    }
                }
            }
    }
    
    public boolean isModuleLoaded(String moduleName)
    {
        return moduleNames.contains(moduleName);
    }
    
    public Set<Module> getModules()
    {
        return modules;
    }
}
