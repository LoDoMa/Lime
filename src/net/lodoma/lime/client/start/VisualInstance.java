package net.lodoma.lime.client.start;

import java.io.File;
import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

import net.lodoma.lime.Lime;
import net.lodoma.lime.client.stage.StageManager;
import net.lodoma.lime.client.stage.menu.MainMenu;
import net.lodoma.lime.client.window.Window;
import net.lodoma.lime.client.window.WindowException;
import net.lodoma.lime.gui.clean.CleanText;
import net.lodoma.lime.gui.clean.CleanUI;
import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.FontHelper;
import net.lodoma.lime.util.OsHelper;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.gfx.FBO;

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
            Lime.LOGGER.C("Failed to create the window");
            Lime.LOGGER.log(e);
            Lime.forceExit(e);
        }

        String fontExtension = OsHelper.isWindows() ? ".ttf" : ".otf";
        
        FontHelper.registerFont(new File(OsHelper.JARPATH + "res/fonts/FreeMono" + fontExtension));
        FontHelper.registerFont(new File(OsHelper.JARPATH + "res/fonts/FreeMonoBold" + fontExtension));
        FontHelper.registerFont(new File(OsHelper.JARPATH + "res/fonts/FreeMonoBoldOblique" + fontExtension));
        FontHelper.registerFont(new File(OsHelper.JARPATH + "res/fonts/FreeMonoOblique" + fontExtension));
        FontHelper.registerFont(new File(OsHelper.JARPATH + "res/fonts/FreeSans" + fontExtension));
        FontHelper.registerFont(new File(OsHelper.JARPATH + "res/fonts/FreeSansBold" + fontExtension));
        FontHelper.registerFont(new File(OsHelper.JARPATH + "res/fonts/FreeSansBoldOblique" + fontExtension));
        FontHelper.registerFont(new File(OsHelper.JARPATH + "res/fonts/FreeSansOblique" + fontExtension));
        FontHelper.registerFont(new File(OsHelper.JARPATH + "res/fonts/FreeSerif" + fontExtension));
        FontHelper.registerFont(new File(OsHelper.JARPATH + "res/fonts/FreeSerifBold" + fontExtension));
        FontHelper.registerFont(new File(OsHelper.JARPATH + "res/fonts/FreeSerifBoldItalic" + fontExtension));
        FontHelper.registerFont(new File(OsHelper.JARPATH + "res/fonts/FreeSerifItalic" + fontExtension));
        
        stageManager = new StageManager();
    }
    
    private void loop()
    {
        stageManager.push(new MainMenu());

        Timer timer = new Timer();

        CleanText[] debugLines = new CleanText[3];
        for (int i = 0; i < debugLines.length; i++)
        	debugLines[i] = new CleanText(new Vector2(0.0f, 0.96f - i * 0.04f), 0.04f, "", CleanUI.FOCUS_TEXT_COLOR, TrueTypeFont.ALIGN_LEFT);
        
        Timer fpsTimer = new Timer();
        double fpsSeconds = 0.0f;
        int frames = 0;
        int fps = 0;
        
        double maxCpuLoad = -1.0f;
        double cpuLoadAver = -1.0f;
        int cpuLoadAverCnt = 0;
        
        while(!Window.closeRequested)
        {
            Window.clear();
            
            timer.update();
            stageManager.update(timer.getDelta());
            stageManager.render();
            
            synchronized (FBO.destroyList)
            {
                for (FBO fbo : FBO.destroyList)
                    fbo.destroy();
                FBO.destroyList.clear();
            }
            
            if(Window.debugEnabled)
            {
                Program.basicProgram.useProgram();
                Program.basicProgram.setUniform("uTexture", UniformType.INT1, 0);
                
                Runtime runtime = Runtime.getRuntime();
                long maxMemory = Math.round(runtime.maxMemory() / (1024.0 * 1024.0));
                long allocatedMemory = Math.round((runtime.totalMemory() - runtime.freeMemory()) / (1024.0 * 1024.0));
                double usage = (long) (allocatedMemory / (double) maxMemory * 1000) / 10.0;
                if (usage > 33)
                {
                    Lime.LOGGER.D("Memory usage over 33%, suggesting GC");
                    System.gc();
                }
                
                OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
                double cpuLoad = osBean.getProcessCpuLoad();
                if (cpuLoad > maxCpuLoad)
                    maxCpuLoad = cpuLoad;
                
                cpuLoadAver = (cpuLoadAver * cpuLoadAverCnt + cpuLoad) / (cpuLoadAverCnt + 1);
                cpuLoadAverCnt++;

                String cpuStr = String.format("%.2f", cpuLoadAver);
                String maxStr = String.format("%.2f", maxCpuLoad);
                String arch = osBean.getArch();
                int pcnt = osBean.getAvailableProcessors();
                
                debugLines[0].text = "fps: " + fps;
                debugLines[1].text = "cpu: " + cpuStr + ", max: " + maxStr + ", arch: " + arch + ", pcnt: " + pcnt;
                debugLines[2].text = "memory: " + allocatedMemory + "/" + maxMemory + " MB, " + usage + "%";
                
                for (int i = 0; i < debugLines.length; i++)
                	debugLines[i].render();
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
                    maxCpuLoad = -1.0f;
                }
                
                if (cpuLoadAverCnt >= 200)
                    cpuLoadAverCnt = 0;
            }
        }
    }
    
    private void clean()
    {
        while (!stageManager.empty())
            stageManager.pop();
        
        Window.close();
    }
    
    public void run()
    {
        init();
        loop();
        clean();
    }
}
