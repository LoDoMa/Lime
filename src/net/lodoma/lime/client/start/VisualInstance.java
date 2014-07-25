package net.lodoma.lime.client.start;

import java.io.File;

import net.lodoma.lime.client.stage.StageManager;
import net.lodoma.lime.client.stage.mainmenu.MainMenu;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.util.FontHelper;
import net.lodoma.lime.util.Timer;

public class VisualInstance
{
    private StageManager stageManager;
    
    private void init()
    {
        Window.setDimensions(800, 600);
        Window.setFullscreen(false);
        Window.setTitle("Lime");
        Window.setFPS(60);
        Window.setOrtho(10.0f, 10.0f);
        
        Window.open();
        
        FontHelper.registerFont(new File("fonts/mytype.ttf"));
        
        stageManager = new StageManager();
    }
    
    private void loop()
    {
        MainMenu mainMenu = new MainMenu(stageManager);
        mainMenu.startStage();
        
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
