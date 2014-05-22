package net.lime.moduletest;

import java.util.List;

import net.lime.moduletest.module.Module;

public class ModuleTestStart
{
    public static void main(String[] args)
    {
        List<Module> modules = ModuleLoader.loadModules();
        for(Module module : modules)
        {
            module.init();
        }
    }
}
