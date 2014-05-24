package net.lodoma.lime.mod.limemod.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import net.lodoma.lime.net.Logic;
import net.lodoma.lime.net.client.generic.GenericClient;
import net.lodoma.lime.net.packet.generic.ClientPacketPool;

public class LimeModuleLogic implements Logic
{
    private List<String> input = Collections.synchronizedList(new ArrayList<String>());
    
    private static class ConsoleReader extends Thread
    {
        private Scanner scanner = new Scanner(System.in);
        private LimeModuleLogic logic;
        
        public ConsoleReader(LimeModuleLogic logic)
        {
            this.logic = logic;
        }
        
        @Override
        public void run()
        {
            super.run();
            while(!isInterrupted())
            {
                String input = scanner.nextLine();
                logic.input.add(input);
            }
        }
    }
    
    private GenericClient client;
    
    private boolean firstIteration = true;
    private ConsoleReader reader;
    
    public LimeModuleLogic(GenericClient client)
    {
        this.client = client;
    }
    
    @Override
    public void logic()
    {
        if(firstIteration)
        {
            reader = new ConsoleReader(this);
            reader.start();
            firstIteration = false;
        }

        while(!input.isEmpty())
        {
            String line = input.get(0);
            input.remove(0);
            
            ClientPacketPool packetPool = (ClientPacketPool) client.getProperty("packetPool");
            packetPool.getPacket("Lime::ChatMessage").send(client, line);
        }
    }
}
