package net.lodoma.lime.client.stage.editor;

import net.lodoma.lime.gui.editor.EditorWindow;
import net.lodoma.lime.gui.editor.EditorWindowPopulator;

public class ColorPickerPopulator implements EditorWindowPopulator
{
    public static final EditorWindowPopulator INSTANCE = new ColorPickerPopulator();
    
    public static final String TITLE = "Color Picker";
    
    private ColorPickerPopulator() {}
    
    @Override
    public String getTitle()
    {
        return TITLE;
    }
    
    @Override
    public void apply(EditorWindow window)
    {
        
    }
}
