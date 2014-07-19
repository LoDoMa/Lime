package net.lodoma.lime.client.io.world;

import java.io.IOException;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientInputHandler;
import net.lodoma.lime.physics.entity.Entity;
import net.lodoma.lime.physics.entity.EntityLoader;
import net.lodoma.lime.world.client.ClientsideWorld;
import net.lodoma.lime.world.material.Material;

public class CIHInitialWorldData extends ClientInputHandler
{
    public CIHInitialWorldData(Client client)
    {
        super(client);
    }

    @Override
    protected void localHandle() throws IOException
    {
        ClientsideWorld world = (ClientsideWorld) client.getProperty("world");
        
        int width = inputStream.readInt();
        int height = inputStream.readInt();
        
        world.reset(width, height);
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++)
            {
                byte info = inputStream.readByte();
                byte shape = inputStream.readByte();
                short material = inputStream.readShort();
                world.setTileInfo(x, y, info);
                world.setTileShape(x, y, shape);
                world.setTileMaterial(x, y, material);
            }
        
        int paletteSize = inputStream.readInt();
        for(int i = 0; i < paletteSize; i++)
        {
            short key = inputStream.readShort();
            long lsb = inputStream.readLong();
            long msb = inputStream.readLong();
            UUID uuid = new UUID(msb, lsb);
            Material material = new Material(uuid);
            world.addPaletteMember(key, material);
        }
        
        EntityLoader entityLoader = (EntityLoader) client.getProperty("entityLoader");
        
        int entityCount = inputStream.readInt();
        for(int i = 0; i < entityCount; i++)
        {
            long id = inputStream.readLong();
            long hash = inputStream.readLong();
            
            try
            {
                Entity entity = entityLoader.loadFromXML(entityLoader.getXMLFileByHash(hash), world);
                entity.setID(id);
                world.addEntity(entity);
                world.createEntity(id);
            }
            catch (SAXException e)
            {
                e.printStackTrace();
            }
            catch (ParserConfigurationException e)
            {
                e.printStackTrace();
            }
        }
        
        world.setRenderReady();
    }
}
