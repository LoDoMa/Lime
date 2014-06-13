package net.lodoma.lime.texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lodoma.lime.util.HashHelper;

public class TexturePool
{
    private Map<Integer, Texture> textures = new HashMap<Integer, Texture>();
    
    public void deleteTextures()
    {
        List<Texture> textureList = new ArrayList<Texture>(textures.values());
        for(Texture texture : textureList)
            texture.delete();
        textures.clear();
    }
    
    public void addTexture(String name, Texture texture)
    {
        int hash = HashHelper.hash32(name);
        if(hash == 0 || textures.containsKey(hash))
            throw new DuplicateTextureHashException(name + ", " + Integer.toHexString(hash));
        textures.put(hash, texture);
    }

    public Texture getTexture(String name)
    {
        int hash = HashHelper.hash32(name);
        return getTexture(hash);
    }
    
    public Texture getTexture(int hash)
    {
        return textures.get(hash);
    }
}
