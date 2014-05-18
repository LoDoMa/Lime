package net.joritan.jlime.stage.editor;

import org.lwjgl.opengl.GL11;

import net.joritan.jlime.stage.Stage;
import net.joritan.jlime.util.Input;
import net.joritan.jlime.util.RenderUtil;

public class PolygonBuilderStage extends Stage
{
    private int vertexCount;
    private float[] verticesX;
    private float[] verticesY;
    
    private float scaleX;
    private float scaleY;
    
    private int selectedVertex;
    
    private String backupContent;
    private String[] textboxContent;
    private int selectedTextbox;
    
    private boolean workingTextbox;
    
    public PolygonBuilderStage(Stage parentStage, Object... manager)
    {
        super(parentStage, null, manager);
    }
    
    @Override
    public void onCreation()
    {
        vertexCount = 3;
        verticesX = new float[vertexCount];
        verticesY = new float[vertexCount];
        scaleX = 1.0f;
        scaleY = 1.0f;
        
        textboxContent = new String[5];
    }
    
    @Override
    public void onDestruction()
    {
        
    }
    
    @Override
    public void onSelection()
    {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f);
        
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }
    
    @Override
    public void onDeselection()
    {
        
    }
    
    @Override
    public void update(float timeDelta)
    {
        if (!workingTextbox)
        {
            if(verticesX.length != vertexCount)
            {
                float[] prevX = verticesX;
                float[] prevY = verticesY;
                verticesX = new float[vertexCount];
                verticesY = new float[vertexCount];
                System.arraycopy(prevX, 0, verticesX, 0, Math.min(prevX.length, verticesX.length));
                System.arraycopy(prevY, 0, verticesY, 0, Math.min(prevY.length, verticesY.length));
                
                selectedVertex %= vertexCount;
            }
            
            textboxContent[0] = vertexCount + "";
            if (vertexCount > 0)
            {
                textboxContent[1] = verticesX[selectedVertex] + "";
                textboxContent[2] = verticesY[selectedVertex] + "";
            }
            textboxContent[3] = scaleX + "";
            textboxContent[4] = scaleY + "";
            
            if (Input.getKeyDown(Input.KEY_X))
                selectedVertex = (selectedVertex + 1) % vertexCount;
            if (Input.getKeyDown(Input.KEY_TAB))
            {
                selectedTextbox = (selectedTextbox + 1) % 5;
            }
            if (Input.getKeyDown(Input.KEY_RETURN))
            {
                textboxContent[selectedTextbox] = "";
                workingTextbox = true;
            }
        }
        else
        {
            if (Input.getKeyDown(Input.KEY_ESCAPE))
                workingTextbox = false;
            if (Input.getKeyDown(Input.KEY_RETURN))
            {
                try
                {
                    if(selectedTextbox == 0)
                        vertexCount = (int) Math.max(3, Integer.parseInt(textboxContent[selectedTextbox]));
                    if(selectedTextbox == 1)
                        verticesX[selectedVertex] = Float.parseFloat(textboxContent[selectedTextbox]);
                    if(selectedTextbox == 2)
                        verticesY[selectedVertex] = Float.parseFloat(textboxContent[selectedTextbox]);
                    if(selectedTextbox == 3)
                        scaleX = Float.parseFloat(textboxContent[selectedTextbox]);
                    if(selectedTextbox == 4)
                        scaleY = Float.parseFloat(textboxContent[selectedTextbox]);
                }
                catch(Exception e)
                {
                    textboxContent[selectedTextbox] = backupContent;
                }
                workingTextbox = false;
            }
            if (Input.getKeyDown(Input.KEY_0))
                textboxContent[selectedTextbox] += '0';
            if (Input.getKeyDown(Input.KEY_1))
                textboxContent[selectedTextbox] += '1';
            if (Input.getKeyDown(Input.KEY_2))
                textboxContent[selectedTextbox] += '2';
            if (Input.getKeyDown(Input.KEY_3))
                textboxContent[selectedTextbox] += '3';
            if (Input.getKeyDown(Input.KEY_4))
                textboxContent[selectedTextbox] += '4';
            if (Input.getKeyDown(Input.KEY_5))
                textboxContent[selectedTextbox] += '5';
            if (Input.getKeyDown(Input.KEY_6))
                textboxContent[selectedTextbox] += '6';
            if (Input.getKeyDown(Input.KEY_7))
                textboxContent[selectedTextbox] += '7';
            if (Input.getKeyDown(Input.KEY_8))
                textboxContent[selectedTextbox] += '8';
            if (Input.getKeyDown(Input.KEY_9))
                textboxContent[selectedTextbox] += '9';
            if (Input.getKeyDown(Input.KEY_PERIOD))
                textboxContent[selectedTextbox] += '.';
        }
    }
    
    @Override
    public void render()
    {
        float[] colorR =
        { 0.3f, 0.3f, 0.3f, 0.3f, 0.3f };
        float[] colorG =
        { 0.3f, 0.3f, 0.3f, 0.3f, 0.3f };
        float[] colorB =
        { 0.3f, 0.3f, 0.3f, 0.3f, 0.3f };
        colorR[selectedTextbox] = colorG[selectedTextbox] = colorB[selectedTextbox] = 0.6f;
        if (workingTextbox)
        {
            colorR[selectedTextbox] = 0.3f;
            colorG[selectedTextbox] = colorB[selectedTextbox] = 0.0f;
        }
        
        RenderUtil.renderTextBox("c: " + textboxContent[0], 0.05f, 0.9f, 0.2f, 0.03f, 0.015f, 0.02f, 1.0f, 1.0f, 1.0f, colorR[0], colorG[0], colorB[0]);
        RenderUtil.renderText("v: " + selectedVertex, 0.05f, 0.86f, 0.015f, 0.02f);
        RenderUtil.renderTextBox("x: " + textboxContent[1], 0.05f, 0.82f, 0.2f, 0.03f, 0.015f, 0.02f, 1.0f, 1.0f, 1.0f, colorR[1], colorG[1], colorB[1]);
        RenderUtil.renderTextBox("y: " + textboxContent[2], 0.05f, 0.78f, 0.2f, 0.03f, 0.015f, 0.02f, 1.0f, 1.0f, 1.0f, colorR[2], colorG[2], colorB[2]);
        
        
        RenderUtil.renderTextBox("s: " + textboxContent[3], 0.05f, 0.66f, 0.2f, 0.03f, 0.015f, 0.02f, 1.0f, 1.0f, 1.0f, colorR[3], colorG[3], colorB[3]);
        RenderUtil.renderTextBox("z: " + textboxContent[4], 0.05f, 0.62f, 0.2f, 0.03f, 0.015f, 0.02f, 1.0f, 1.0f, 1.0f, colorR[4], colorG[4], colorB[4]);
    }
}
