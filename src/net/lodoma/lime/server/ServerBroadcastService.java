package net.lodoma.lime.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ServerBroadcastService implements Runnable
{
    private Thread thread;
    private boolean running;
    
    private DatagramSocket socket;
    
    private Server server;
    
    public ServerBroadcastService(Server server)
    {
        this.server = server;
    }
    
    private void openService()
    {
        try
        {
            socket = new DatagramSocket(NetSettings.PORT, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
        }
        catch (IOException e)
        {
            server.setCloseMessage("Service failed to open");
            server.closeInThread();
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
    }
    
    public void stop()
    {
        if(!running) return;
        running = false;
        closeService();
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
                    DatagramPacket sendPacket = new DatagramPacket(acknowledge, acknowledge.length, packet.getAddress(), packet.getPort());
                    socket.send(sendPacket);
                    System.out.println("Responded to broadcast enquiry from " + packet.getAddress().getHostAddress());
                }
            }
        }
        catch (SocketException e)
        {
            if (!e.getMessage().equals("Socket closed") && running)
            {
                server.setCloseMessage("Broadcast service exception");
                server.closeInThread();
            }
        }
        catch (Exception e)
        {
            if (running)
            {
                server.setCloseMessage("Broadcast service exception");
                server.closeInThread();
            }
        }
    }
}
