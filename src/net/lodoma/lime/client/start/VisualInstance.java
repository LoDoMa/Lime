package net.lodoma.lime.client.start;

import java.awt.Font;
import java.io.File;

import net.lodoma.lime.client.stage.StageManager;
import net.lodoma.lime.client.stage.login.Login;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.client.window.WindowException;
import net.lodoma.lime.gui.Color;
import net.lodoma.lime.gui.Text;
import net.lodoma.lime.texture.Texture;
import net.lodoma.lime.util.FontHelper;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class VisualInstance
{
    private StageManager stageManager;
    
    private void init()
    {
        Window.size = new Vector2(800, 450);
        Window.resolution = new Vector2(16, 9);
        Window.resizable = true;
        Window.fullscreen = false;
        Window.title = "Lime";
        Window.debugEnabled = false;
        Window.vsync = true;
        
        try
        {
            Window.create();
        }
        catch(WindowException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        
        Texture.init();
        FontHelper.registerFont(new File("fonts/mytype.ttf"));
        
        stageManager = new StageManager();
    }
    
    private void loop()
    {
        new Login(stageManager).startStage();

        Timer timer = new Timer();

        Text debugText = new Text(0.0f, 0.96f, 0.03f, 0.03f, "", new Color(1.0f, 1.0f, 1.0f), "My type of font", Font.PLAIN, TrueTypeFont.ALIGN_LEFT);
        Timer fpsTimer = new Timer();
        double fpsSeconds = 0.0f;
        int frames = 0;
        int fps = 0;
        
        while(!Window.closeRequested)
        {
            Window.clear();
            
            timer.update();
            stageManager.update(timer.getDelta());
            stageManager.render();
            
            if(Window.debugEnabled)
            {
                debugText.setText("fps " + fps);
                debugText.render();
            }
            
            Window.update();

            if(Window.debugEnabled)
            {
                frames++;
                fpsTimer.update();
                fpsSeconds += timer.getDelta();
                if(fpsSeconds >= 1.0)
                {
                    fpsSeconds -= 1.0;
                    fps = frames;
                    frames = 0;
                }
            }
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
