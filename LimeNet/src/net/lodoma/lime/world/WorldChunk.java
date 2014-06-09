package net.lodoma.lime.world;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.lodoma.lime.server.generic.ServerUser;
import net.lodoma.lime.server.generic.UserPool;

public class WorldChunk
{
    private ServersideWorld world;
    
    private int width;
    private int height;
    
    private byte[] info;
    private byte[] shape;
    private short[] material;
    
    private UserPool userPool;
    private Map<ServerUser, Set<Long>> changes;
    
    public WorldChunk(ServersideWorld world, int width, int height, UserPool userPool)
    {
        this.world = world;
        
        this.width = width;
        this.height = height;
        
        this.info = new byte[width * height];
        this.shape = new byte[width * height];
        this.material = new short[width * height];
        
        this.userPool = userPool;
        this.changes = new HashMap<ServerUser, Set<Long>>();
    }
    
    private void reportChange(int x, int y)
    {
        Set<ServerUser> users = userPool.getUserSet();
        for(ServerUser user : users)
            changes.get(user).add((long) x << 32 | y);
    }
    
    public byte getInfo(int x, int y)
    {
        return info[y * width + x];
    }
    
    public byte getShape(int x, int y)
    {
        return shape[y * width + x];
    }
    
    public short getMaterial(int x, int y)
    {
        return material[y * width + x];
    }
    
    public void setInfo(int x, int y, byte v)
    {
        if(info[y * width + x] != v)
        {
            info[y * width + x] = v;
            reportChange(x, y);
        }
    }
    
    public void setShape(int x, int y, byte v)
    {
        if(shape[y * width + x] != v)
        {
            shape[y * width + x] = v;
            reportChange(x, y);
        }
    }
    
    public void setMaterial(int x, int y, short v)
    {
        if(material[y * width + x] != v)
        {
            material[y * width + x] = v;
            reportChange(x, y);
        }
    }
    
    public void activeUser(ServerUser user)
    {
        if(!changes.containsKey(user))
        {
            Set<Long> set = new HashSet<Long>();
            for(int x = 0; x < width; x++)
                for(int y = 0; y < height; y++)
                    set.add((long) x << 32 | y);
            changes.put(user, set);
        }
    }
    
    public void inactiveUser(ServerUser user)
    {
        if(changes.containsKey(user))
            changes.remove(user);
    }
    
    public void update()
    {
        
    }
}
