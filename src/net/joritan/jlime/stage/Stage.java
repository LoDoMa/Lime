package net.joritan.jlime.stage;

import net.joritan.jlime.stage.root.BlueScreen;

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
            if(manager.length != 1) new BlueScreen(null, new IllegalArgumentException(), "illegal argument");
            if(!(manager[0] instanceof StageManager)) new BlueScreen(null, new IllegalArgumentException(), "illegal argument");
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
