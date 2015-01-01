package net.lodoma.lime.world.entity;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

import org.jbox2d.collision.shapes.CircleShape;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.script.LuaScript;
import net.lodoma.lime.script.library.EntityFunctions;
import net.lodoma.lime.script.library.EventFunctions;
import net.lodoma.lime.script.library.LimeLibrary;
import net.lodoma.lime.script.library.UtilFunctions;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.World;

public class Entity implements Identifiable<Integer>
{
    public int identifier;
    
    public LuaScript script;
    public World world;
    
    public EntityBody body;
    public EntityShape shape;
    
    public Entity(World world, Server server)
    {
        this.world = world;
        body = new EntityBody();
    }
    
    public Entity(World world, int identifier, Client client)
    {
        this.world = world;
        this.identifier = identifier;
        
        shape = new EntityShape();
    }
    
    @Override
    public Integer getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        this.identifier = identifier;
    }
    
    public void destroy()
    {
        if (body != null)
            body.destroy();
    }
    
    public void assignScript(Server server, String scriptName)
    {
        LimeLibrary library = new LimeLibrary(server);
        UtilFunctions.addToLibrary(library);
        EntityFunctions.addToLibrary(library);
        EventFunctions.addToLibrary(library);
        
        script = new LuaScript(library);
        
        try
        {
            script.load(new File("./script/entity/" + scriptName + ".lua"));
        }
        catch (IOException e)
        {
            // TODO: handle this
            e.printStackTrace();
        }
        
        init();
    }
    
    public void init()
    {
        script.call("Lime_Init", new Object[] { identifier });
    }
    
    public void update(double timeDelta)
    {
        script.call("Lime_Update", new Object[] { identifier, timeDelta, false });
    }
    
    public void debugRender()
    {
        shape.debugRender();
    }
    
    public void render()
    {
        throw new UnsupportedOperationException();
    }
    
    public void acceptSnapshotCompo(ByteBuffer snapshotCompo)
    {
        int compoc = snapshotCompo.getInt();
        if (shape.positionList.length != compoc)
            shape.positionList = new Vector2[compoc];
        if (shape.angleList.length != compoc)
            shape.angleList = new float[compoc];
        if (shape.radiusList.length != compoc)
            shape.radiusList = new float[compoc];
        
        for (int i = 0; i < compoc; i++)
        {
            float positionX = snapshotCompo.getFloat();
            float positionY = snapshotCompo.getFloat();
            float angle = snapshotCompo.getFloat();
            float radius = snapshotCompo.getFloat();
            
            if (shape.positionList[i] == null)
                shape.positionList[i] = new Vector2();
            shape.positionList[i].set(positionX, positionY);
            shape.angleList[i] = angle;
            shape.radiusList[i] = radius;
        }
    }
    
    public byte[] buildSnapshotCompo(boolean forced)
    {
        // NOTE: this is always sending full snapshots
        
        int compoc = body.components.size();
        ByteBuffer snapshotCompo = ByteBuffer.allocate(8 + 16 * compoc);
        snapshotCompo.putInt(identifier);
        snapshotCompo.putInt(compoc);
        
        body.components.foreach(new Consumer<BodyComponent>()
        {
            @Override
            public void accept(BodyComponent component)
            {
                snapshotCompo.putFloat(component.engineBody.getPosition().x);
                snapshotCompo.putFloat(component.engineBody.getPosition().y);
                snapshotCompo.putFloat(component.engineBody.getAngle());
                snapshotCompo.putFloat(((CircleShape) component.engineFixture.m_shape).m_radius);
            }
        });
        
        return snapshotCompo.array();
    }
}
