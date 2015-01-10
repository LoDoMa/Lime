package net.lodoma.lime.client.logic;

import java.nio.ByteBuffer;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacket;
import net.lodoma.lime.client.packet.CPHSnapshot;
import net.lodoma.lime.client.packet.CPInputState;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.gfx.WorldRenderer;

public class CLWorld implements ClientLogic
{
    public static final double INPUT_PS = 20;
    public static final double INPUT_MAXTIME = 1.0 / INPUT_PS;
    public double inputTime = INPUT_MAXTIME;
    
    private Client client;
    
    private Timer timer;
    
    public ClientPacket inputStatePacket;
    
    @Override
    public void init(Client client)
    {
        this.client = client;
        client.world = new World();
        client.worldRenderer = new WorldRenderer(client.world);
        
        client.cpPool.add(new CPInputState(client));
        client.cphPool.add(new CPHSnapshot(client));
        
        inputStatePacket = client.cpPool.get(CPInputState.HASH);
    }
    
    @Override
    public void clean()
    {
        client.world.clean();
    }
    
    @Override
    public void logic()
    {
        if(timer == null) timer = new Timer();
        timer.update();
        double timeDelta = timer.getDelta();
        
        inputTime -= timeDelta;
        if (inputTime <= 0.0)
        {
            Input.update();
            ByteBuffer state = Input.getState();
            inputStatePacket.write(state);
        }
        while (inputTime <= 0.0)
            inputTime += INPUT_MAXTIME;
    }
}
