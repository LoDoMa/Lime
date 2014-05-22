package another.author;

import net.lime.moduletest.module.InitBundle;
import net.lime.moduletest.module.Module;
import net.lime.moduletest.module.ModuleInit;

@Module(name = MyModule.MODULE_NAME, author = "just somebody that I used to know")
public class MyModule
{
    public static final String MODULE_NAME = "MyAmazingModule";
    
    @ModuleInit
    public void initModule(InitBundle bundle)
    {
        System.out.println("HIII");
    }
}
