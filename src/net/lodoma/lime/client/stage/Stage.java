package net.lodoma.lime.client.stage;

public abstract class Stage
{
    protected final StageManager manager;
    
    public Stage(StageManager manager) 
    {
        this.manager = manager;
    }
    
    public final StageManager getManager()
    {
        return manager;
    }
    
    public final void startStage()
    {
        preStart();
        manager.pushStage(this);
        onStart();
    }
    
    public final void endStage()
    {
        onEnd();
        manager.popStage();
        postEnd();
    }
    
    public abstract void preStart();
    public abstract void onStart();
    public abstract void onEnd();
    public abstract void postEnd();
    
    public abstract void update(double timeDelta);
    public abstract void render();
}
