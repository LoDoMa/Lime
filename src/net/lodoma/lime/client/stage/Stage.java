package net.lodoma.lime.client.stage;

public abstract class Stage
{
    protected final StageManager manager;
    
    public Stage(StageManager manager) 
    {
        this.manager = manager;
    }
    
    public final void startStage()
    {
        manager.pushStage(this);
        onStart();
    }
    
    public final void endStage()
    {
        onEnd();
        manager.popStage();
    }
    
    public abstract void onStart();
    public abstract void onEnd();
    
    public abstract void update(double timeDelta);
    public abstract void render();
}
