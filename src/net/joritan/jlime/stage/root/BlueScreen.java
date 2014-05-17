package net.joritan.jlime.stage.root;

import net.joritan.jlime.stage.StageManager;
import net.joritan.jlime.util.RenderUtil;
import org.lwjgl.opengl.GL11;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class BlueScreen
{
    private static final float TEXT_W = 0.015f;
    private static final float TEXT_H = 0.020f;
    
    private static StageManager manager;
    
    public static void setDefaultManager(StageManager manager)
    {
        BlueScreen.manager = manager;
    }
    
    private RootStage stage;
    
    private List<String> lines = new ArrayList<String>();
    
    public BlueScreen(StageManager manager, Exception exception)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String message = sw.toString();
        message = message.toLowerCase();
        message = exception.getMessage() + "\n" + message;
        imp(manager, message.split("\n"));
    }
    
    public BlueScreen(StageManager manager, Exception exception, String... additional)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String message = sw.toString();
        message = message.toLowerCase();
        for (String a : additional)
            message = a + "\n" + message;
        message = exception.getMessage() + "\n" + message;
        imp(manager, message.split("\n"));
    }
    
    public BlueScreen(StageManager manager, String... message)
    {
        imp(manager, message);
    }
    
    private void imp(StageManager manager, String... message)
    {
        if (manager == null)
            manager = BlueScreen.manager;
        
        stage = manager.reachRootStage();
        stage.setBluescreen(this);
        
        lines.add("jlime has stopped running because it encountered a problem");
        lines.add("");
        lines.add("--- begin error report ---");
        int maxLetterWidth = (int) (0.94f / TEXT_W);
        for (String msg : message)
        {
            while (msg.length() > maxLetterWidth)
            {
                String sub = msg.substring(0, maxLetterWidth);
                lines.add(sub);
                msg = msg.substring(maxLetterWidth, msg.length());
            }
            lines.add(msg);
        }
        lines.add("--- end error report ---");
        lines.add("");
        lines.add("it is safe to close the application");
    }
    
    public void render()
    {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 1, 0, 1, -1, 1);
        
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        
        GL11.glClearColor(0.0f, 0.0f, 0.6f, 1.0f);
        
        float msgy = 0.97f;
        for (String line : lines)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.01f, msgy, 0.0f);
            RenderUtil.renderText(line, TEXT_W, TEXT_H);
            GL11.glPopMatrix();
            msgy -= 0.025f;
        }
    }
}
