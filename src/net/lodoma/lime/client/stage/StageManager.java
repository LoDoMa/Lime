package net.lodoma.lime.client.stage;

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
            leaf.parent.onInactive();
        this.leaf.onActive();
        reupdate = true;
    }
    
    public void pop()
    {
        leaf.onInactive();
        leaf = leaf.parent;
        if (leaf.parent != null)
            leaf.parent.onActive();
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
