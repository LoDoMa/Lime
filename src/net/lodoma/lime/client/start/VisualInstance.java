package net.lodoma.lime.client.start;

import java.io.File;

import net.lodoma.lime.client.stage.StageManager;
import net.lodoma.lime.client.stage.login.Login;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.util.FontHelper;
import net.lodoma.lime.util.Timer;

public class VisualInstance
{
    private StageManager stageManager;
    
    private void init()
    {
        Window.setWindowSize(800, 450);
        Window.setResolution(16, 9);
        Window.setFullscreen(false);
        Window.setTitle("Lime");
        Window.setFPS(60);
        
        Window.open();
        
        FontHelper.registerFont(new File("fonts/mytype.ttf"));
        
        stageManager = new StageManager();
    }
    
    private void loop()
    {
        new Login(stageManager).startStage();
        
        Timer timer = new Timer();
        while(!Window.isCloseRequested())
        {
            Window.clear();
            
            timer.update();
            stageManager.update(timer.getDelta());
            stageManager.render();
            
            Window.update();
        }
    }
    
    private void clean()
    {
        stageManager.popAll();
        
        Window.close();
    }
    
    public void run()
    {
        init();
        loop();
        clean();
    }
}
