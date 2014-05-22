package net.lime.moduletest.module;

public abstract class Module
{
    protected String moduleName = null;
    protected String moduleAuthor = null;
    
    final void checkValid()
    {
        if(moduleName == null)
            System.err.println("missing module name");
        if(moduleAuthor == null)
            System.err.println("missing module author");
    }
    public abstract void init();
}
