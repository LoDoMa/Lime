package net.lodoma.lime.gui.exp.clean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.lodoma.lime.gui.exp.UICallback;
import net.lodoma.lime.gui.exp.UIGroup;
import net.lodoma.lime.gui.exp.UIObject;
import net.lodoma.lime.gui.exp.layout.UIVerticalFlowLayout;
import net.lodoma.lime.util.TrueTypeFont;
import net.lodoma.lime.util.Vector2;

public class CleanList<T> extends UIObject
{
    private class CleanListElement extends CleanToggle
    {
        public final T item;
        
        public CleanListElement(T item, CleanList<T> list)
        {
            super(new Vector2(0.0f, 0.0f), new Vector2(list.getDimensions().x, list.elemHeight), item.toString(), list.group, list.alignment);
            this.item = item;
        }
    }
    
    private UIGroup<CleanListElement> group;
    private int alignment;
    private float elemHeight;
    
    public CleanList(Vector2 position, Vector2 dimensions, UICallback onSelect)
    {
        getLocalPosition().set(position);
        getLocalDimensions().set(dimensions);
        
        group = new UIGroup<CleanListElement>(onSelect);
        alignment = TrueTypeFont.ALIGN_LEFT;
        elemHeight = 0.05f;
        
        setLayout(new UIVerticalFlowLayout(-0.00001f));
    }
    
    public void addElement(T item)
    {
        addChild(new CleanListElement(item, this));
    }
    
    @SuppressWarnings("unchecked")
    public void removeElement(T item, Comparator<T> comparator)
    {
        foreachChild((UIObject object, Integer index) -> {
            if (comparator.compare(((CleanListElement) object).item, item) == 0)
            {
                removeChild(object);
                return true;
            }
            return false;
        });
    }
    
    @SuppressWarnings("unchecked")
    public List<T> getItemList()
    {
        List<T> list = new ArrayList<T>();
        foreachChild((UIObject object, Integer index) -> {
            list.add(((CleanListElement) object).item);
            return false;
        });
        return list;
    }
    
    public T getSelectedItem()
    {
        return group.selected.item;
    }
}
