package net.lodoma.lime.client.logic;

import java.io.DataInputStream;
import java.nio.ByteBuffer;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.client.ClientPacket;
import net.lodoma.lime.client.packet.CPHSnapshot;
import net.lodoma.lime.client.packet.CPInputState;
import net.lodoma.lime.input.Input;
import net.lodoma.lime.util.Timer;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.gfx.WorldRenderer;

public class CLGame extends ClientLogic
{
    public static final double INPUT_PS = 20;
    public static final double INPUT_MAXTIME = 1.0 / INPUT_PS;
    public double inputTime = INPUT_MAXTIME;
    
    private Timer timer;
    
    public ClientPacket inputStatePacket;
    
    public CLGame(Client client)
    {
        super(client);
    }
    
    @Override
    public void init()
    {
        client.world = new World();
        client.worldRenderer = new WorldRenderer(client.world);
        
        client.cpPool.add(new CPInputState(client));
        client.cphPool.add(new CPHSnapshot(client));
        
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
    public void handleSnapshot(DataInputStream inputStream)
    {
        
    }
}
