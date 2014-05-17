package net.joritan.esc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagContainer extends Tag
{
    private Map<String, Tag> data = new HashMap<String, Tag>();

    public TagContainer()
    {
        super();
    }

    public TagContainer(String name)
    {
        super(name);
    }

    public void addTag(Tag tag)
    {
        data.put(tag.getName(), tag);
    }

    public Map<String, Tag> getTags()
    {
        return data;
    }

    @Override
    public void load(ESCInputStream stream)
    {
        super.load(stream);

        byte tag;
        while((tag = stream.readByte()) != 0)
        {
            Tag newTag = null;
            switch(tag)
            {
                case 1: newTag = new TagByte(); break;
                case 2: newTag = new TagShort(); break;
                case 3: newTag = new TagInt(); break;
                case 4: newTag = new TagLong(); break;
                case 5: newTag = new TagFloat(); break;
                case 6: newTag = new TagDouble(); break;
                case 7: newTag = new TagString(); break;
                case 8: newTag = new TagContainer(); break;
            }
            newTag.load(stream);
            data.put(newTag.getName(), newTag);
        }
    }

    @Override
    public void store(ESCOutputStream stream)
    {
        super.store(stream);
        List<Tag> tags = new ArrayList<Tag>(data.values());
        for(Tag tag : tags)
        {
            if(tag instanceof TagByte) stream.writeByte((byte) 1);
            if(tag instanceof TagShort) stream.writeByte((byte) 2);
            if(tag instanceof TagInt) stream.writeByte((byte) 3);
            if(tag instanceof TagLong) stream.writeByte((byte) 4);
            if(tag instanceof TagFloat) stream.writeByte((byte) 5);
            if(tag instanceof TagDouble) stream.writeByte((byte) 6);
            if(tag instanceof TagString) stream.writeByte((byte) 7);
            if(tag instanceof TagContainer) stream.writeByte((byte) 8);
            tag.store(stream);
        }
        stream.writeByte((byte) 0);
    }
}
