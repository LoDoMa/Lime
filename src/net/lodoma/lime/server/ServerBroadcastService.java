package net.lodoma.lime.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import net.lodoma.lime.Lime;

public class ServerBroadcastService implements Runnable
{
    private Thread thread;
    private boolean running;
    
    private DatagramSocket socket;
    
    public ServerBroadcastService(Server server)
    {
        
    }
    
    private void openService()
    {
        try
        {
            socket = new DatagramSocket(NetSettings.PORT, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            Lime.LOGGER.F("Opened socket; port = " + NetSettings.PORT + ", address = 0.0.0.0, broadcast");
        }
        catch (IOException e)
        {
            Lime.LOGGER.C("Failed to open broadcast service");
            Lime.LOGGER.log(e);
            Lime.forceExit(e);
        }
    }
    
    private void closeService()
    {
        if(socket != null && !socket.isClosed())
            socket.close();
    }
    
    public void start()
    {
        if(running) return;
        running = true;
        openService();
        thread = new Thread(this, "ServerBroadcastThread");
        thread.start();
        Lime.LOGGER.F("Broadcast service started");
    }
    
    public void stop()
    {
        if(!running) return;
        running = false;
        closeService();
        Lime.LOGGER.F("Broadcast service closed");
    }
    
    @Override
    public void run()
    {
        ByteBuffer enquiryBuffer = ByteBuffer.allocate(Long.BYTES);
        enquiryBuffer.putLong(NetSettings.BROADCAST_ENQUIRY);
        enquiryBuffer.flip();
        byte[] enquiry = enquiryBuffer.array();
        
        ByteBuffer acknowledgeBuffer = ByteBuffer.allocate(Long.BYTES);
        acknowledgeBuffer.putLong(NetSettings.BROADCAST_ACKNOWLEDGE);
        acknowledgeBuffer.flip();
        byte[] acknowledge = acknowledgeBuffer.array();
        
        try
        {
            while (true)
            {
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(packet);
                
                byte[] data = packet.getData();

                if (Arrays.equals(Arrays.copyOfRange(data, 0, enquiry.length), enquiry))
                {
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    Lime.LOGGER.I("Enquiry from " + address + ":" + port);
                    DatagramPacket sendPacket = new DatagramPacket(acknowledge, acknowledge.length, address, port);
                    socket.send(sendPacket);
                }
            }
        }
        catch (SocketException e)
        {
            if (!e.getMessage().equals("Socket closed") && running)
            {
                Lime.LOGGER.C("Socket exception in broadcast service");
                Lime.LOGGER.log(e);
                Lime.forceExit(e);
            }
        }
        catch (Exception e)
        {
            if (running)
            {
                Lime.LOGGER.C("Unexpected exception in broadcast service");
                Lime.LOGGER.log(e);
                Lime.forceExit(e);
            }
        }
    }
}
