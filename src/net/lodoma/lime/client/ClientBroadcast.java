package net.lodoma.lime.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import net.lodoma.lime.server.NetSettings;

public class ClientBroadcast
{
    private DatagramSocket socket;
    
    public ClientBroadcast()
    {
        try
        {
            broadcast();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void broadcast() throws IOException
    {
        socket = new DatagramSocket();
        socket.setBroadcast(true);
        socket.setSoTimeout(NetSettings.BROADCAST_TIMEOUT);
        
        int port = NetSettings.PORT;
        
        ByteBuffer enquiryBuffer = ByteBuffer.allocate(Long.BYTES);
        enquiryBuffer.putLong(NetSettings.BROADCAST_ENQUIRY);
        enquiryBuffer.flip();
        byte[] enquiry = enquiryBuffer.array();
        
        DatagramPacket packet255 = new DatagramPacket(enquiry, enquiry.length, InetAddress.getByName("255.255.255.255"), port);
        socket.send(packet255);
        
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements())
        {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (networkInterface.isLoopback() || !networkInterface.isUp())
                continue; // We don't want to broadcast to the loopback interface
            
            List<InterfaceAddress> addresses = networkInterface.getInterfaceAddresses();
            for (InterfaceAddress interfaceAddress : addresses)
            {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast == null)
                    continue;
                
                DatagramPacket packet = new DatagramPacket(enquiry, enquiry.length, broadcast, port);
                socket.send(packet);
            }
        }
        
        ByteBuffer acknowledgeBuffer = ByteBuffer.allocate(Long.BYTES);
        acknowledgeBuffer.putLong(NetSettings.BROADCAST_ACKNOWLEDGE);
        acknowledgeBuffer.flip();
        byte[] acknowledge = acknowledgeBuffer.array();
        
        while (true)
        {
            byte[] receiveBuffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            
            try
            {
                socket.receive(packet);
            }
            catch (SocketTimeoutException e)
            {
                break;
            }
            
            if (Arrays.equals(Arrays.copyOfRange(receiveBuffer, 0, acknowledge.length), acknowledge))
                System.out.println("Broadcast response from " + packet.getAddress().getHostAddress());
        }
        
        socket.close();
    }
}
