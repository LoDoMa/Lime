package net.lodoma.lime.client.logic;

import java.nio.ByteBuffer;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacket;
import net.lodoma.lime.client.packet.CPInputState;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.snapshot.Snapshot;
import net.lodoma.lime.snapshot.SnapshotData;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.WorldSnapshotSegment;
import net.lodoma.lime.world.gfx.WorldRenderer;

public class CLGame extends ClientLogic
{
    public static final String NAME = "Lime::Game";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public static final double INPUT_PS = 20;
    public static final double INPUT_MAXTIME = 1.0 / INPUT_PS;
    public double inputTime = INPUT_MAXTIME;
    
    private Timer timer;
    
    public ClientPacket inputStatePacket;
    
    public CLGame(Client client)
    {
        super(client, HASH);
    }
    
    @Override
    public void init()
    {
        client.world = new World();
        client.worldRenderer = new WorldRenderer(client.world);
        
        inputStatePacket = client.cpPool.get(CPInputState.HASH);
    }
    
    @Override
    public void destroy()
    {
        client.world.clean();
    }
    
    @Override
    public void update()
    {
        if (timer == null) timer = new Timer();
        timer.update();
        double timeDelta = timer.getDelta();
        
        inputTime -= timeDelta;
        if (inputTime <= 0.0)
        {
            Input.update();
            ByteBuffer state = Input.inputData.getState();
            inputStatePacket.write(state);
        }
        while (inputTime <= 0.0)
            inputTime += INPUT_MAXTIME;
    }
    
    @Override
    public void render()
    {
        if(client.worldRenderer != null)
            client.worldRenderer.render();
    }
    
    @Override
    public SnapshotData createSnapshotData()
    {
        return new WorldSnapshotSegment();
    }
    
    @Override
    public void handleSnapshot(Snapshot snapshot)
    {
        client.world.applySnapshot((WorldSnapshotSegment) snapshot.data, client);
    }
}
