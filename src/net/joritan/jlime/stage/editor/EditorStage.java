package net.joritan.jlime.stage.editor;

import net.joritan.jlime.stage.Stage;
import net.joritan.jlime.stage.root.BlueScreen;

public class EditorStage extends Stage
{
    public EditorStage(Stage parentStage, Object... manager)
    {
        super(parentStage, null, manager);
    }

    @Override
    public void onCreation()
    {

    }

    @Override
    public void onDestruction()
    {

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
        new BlueScreen(manager, new Exception());
        /*
        new BlueScreen(manager, new String[]
        {
                "stage manager encountered a problem",
                "problem area: execution transfer",
                "jlime tried to tranfer execution to an unregistered stage"
        });
        */
    }

    @Override
    public void render()
    {

    }
}
