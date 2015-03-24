package net.lodoma.lime.client.stage;

import net.lodoma.lime.Lime;

public class StageManager
{
    public Stage leaf;

    private boolean reupdate;
    
    public void push(Stage leaf)
    {
        leaf.manager = this;
        leaf.parent = this.leaf;
        this.leaf = leaf;
        if (leaf.parent != null)
        {
            leaf.parent.onInactive();
            Lime.LOGGER.D("Stage " + leaf.parent + " inactive");
        }
        this.leaf.onActive();
        Lime.LOGGER.D("Stage " + this.leaf + " active");
        reupdate = true;
    }
    
    public void pop()
    {
        leaf.onInactive();
        Lime.LOGGER.D("Stage " + leaf + " inactive");
        leaf = leaf.parent;
        if (leaf != null)
        {
            leaf.onActive();
            Lime.LOGGER.D("Stage " + leaf + " active");
        }
        reupdate = true;
    }
    
    public boolean empty()
    {
        return leaf == null;
    }
    
    public void update(double timeDelta)
    {
        reupdate = true;
        while (reupdate)
        {
            reupdate = false;
            if (leaf != null)
                leaf.update(timeDelta);
        }
    }
    
    public void render()
    {
        leaf.render();
    }
}
