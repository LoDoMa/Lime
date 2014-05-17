package net.joritan.jlime.stage.singleplayer;

import net.joritan.jlime.stage.Stage;
import net.joritan.jlime.stage.singleplayer.world.Environment;
import net.joritan.jlime.util.Texture;

public class SingleplayerStage extends Stage
{
    private Environment environment;

    public SingleplayerStage(Stage parentStage, Object... manager)
    {
        super(parentStage, new SingleplayerLoader(), manager);
    }

    @Override
    public void onCreation()
    {
        Texture.addTexture("dirt", new Texture("res/dirt2.png"));

        environment = new Environment();
    }

    @Override
    public void onDestruction()
    {
        environment.destroy();

        Texture.getTexture("dirt").unload();
        Texture.removeTexture("dirt");
    }

    @Override
    public void onSelection()
    {

    }

    @Override
    public void onDeselection()
    {

    }

    @Override
    public void update(float timeDelta)
    {
        environment.update(timeDelta);
    }

    @Override
    public void render()
    {
        environment.render();
    }
}
