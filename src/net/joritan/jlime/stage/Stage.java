package net.joritan.jlime.stage;

public abstract class Stage
{
    public final Stage parentStage;
    public final StageLoader loader;
    public final StageManager manager;

    private boolean hasLoaded;

    public Stage(Stage parentStage, StageLoader loader, Object... manager)
    {
        this.parentStage = parentStage;
        this.loader = loader;
        if(parentStage == null)
        {
            if(manager.length != 1) throw new IllegalArgumentException();
            if(!(manager[0] instanceof StageManager)) throw new IllegalArgumentException();
            this.manager = (StageManager) manager[0];
        }
        else this.manager = parentStage.manager;

        hasLoaded = false;
        if(loader == null) finishLoading();
        else loader.stageStart(this);
    }

    void finishLoading()
    {
        hasLoaded = true;
    }

    public abstract void onCreation();
    public abstract void onDestruction();

    public abstract void onSelection();
    public abstract void onDeselection();

    public abstract void update(float timeDelta);
    public abstract void render();

    public final void manageUpdate(float timeDelta)
    {
        if(hasLoaded) update(timeDelta);
    }

    public final void manageRender()
    {
        if(hasLoaded) render();
        else loader.render();
    }
}
