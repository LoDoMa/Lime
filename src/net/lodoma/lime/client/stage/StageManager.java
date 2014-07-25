package net.lodoma.lime.client.stage;

import java.util.Stack;

import net.lodoma.lime.input.Input;

public class StageManager
{
    private Stack<Stage> stack;
    
    public StageManager()
    {
        stack = new Stack<Stage>();
    }
    
    public void pushStage(Stage stage)
    {
        stack.push(stage);
    }
    
    public void popStage()
    {
        stack.pop();
    }
    
    public void popAll()
    {
        while(!stack.empty())
            stack.peek().endStage();
    }
    
    public void update(double timeDelta)
    {
        if(!stack.isEmpty())
            stack.peek().update(timeDelta);
        Input.update();
    }
    
    public void render()
    {
        if(!stack.isEmpty())
            stack.peek().render();
    }
}
