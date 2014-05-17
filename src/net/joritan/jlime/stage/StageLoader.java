package net.joritan.jlime.stage;

public abstract class StageLoader extends Thread
{
    private Stage stage;

    @Override
    public void run()
    {
        load();
        stage.finishLoading();
    }

    public final void stageStart(Stage stage)
    {
        this.stage = stage;
        this.start();
    }

    public abstract void load();
    public abstract void render();
}
