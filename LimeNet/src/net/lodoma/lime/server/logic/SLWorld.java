package net.lodoma.lime.server.logic;

import net.lodoma.lime.server.generic.GenericServer;
import net.lodoma.lime.server.generic.net.packet.ServerPacketPool;
import net.lodoma.lime.world.builder.WorldFileLoader;
import net.lodoma.lime.world.server.ServersideWorld;
import net.lodoma.lime.world.server.packet.SPHWorldChunkInformationRequest;
import net.lodoma.lime.world.server.packet.SPHWorldChunkRequest;
import net.lodoma.lime.world.server.packet.SPHWorldDimensionRequest;
import net.lodoma.lime.world.server.packet.SPHWorldPaletteRequest;
import net.lodoma.lime.world.server.packet.SPWorldChunk;
import net.lodoma.lime.world.server.packet.SPWorldChunkInformation;
import net.lodoma.lime.world.server.packet.SPWorldDimensions;
import net.lodoma.lime.world.server.packet.SPWorldPalette;

public class SLWorld implements ServerLogic
{
    private GenericServer server;
    private ServerPacketPool packetPool;
    private ServersideWorld world;
    
    @Override
    public void baseInit(GenericServer server)
    {
        this.server = server;
    }
    
    @Override
    public void propertyInit()
    {
        server.setProperty("world", new ServersideWorld(server));
    }
    
    @Override
    public void fetchInit()
    {
        packetPool = (ServerPacketPool) server.getProperty("packetPool");
        world = (ServersideWorld) server.getProperty("world");
        world.fetch();
    }
    
    @Override
    public void generalInit()
    {
        packetPool.addHandler("Lime::WorldDimensionRequest", new SPHWorldDimensionRequest());
        packetPool.addPacket("Lime::WorldDimensions", new SPWorldDimensions());
        
        packetPool.addHandler("Lime::WorldPaletteRequest", new SPHWorldPaletteRequest());
        packetPool.addPacket("Lime::WorldPalette", new SPWorldPalette());

        packetPool.addHandler("Lime::WorldChunkInformationRequest", new SPHWorldChunkInformationRequest());
        packetPool.addPacket("Lime::WorldChunkInformation", new SPWorldChunkInformation());

        packetPool.addHandler("Lime::WorldChunkRequest", new SPHWorldChunkRequest());
        packetPool.addPacket("Lime::WorldChunk", new SPWorldChunk());
        
        WorldFileLoader fileLoader = new WorldFileLoader();
        fileLoader.build(world);
    }
    
    @Override
    public void clean()
    {
        
    }
    
    @Override
    public void logic()
    {
        
    }
}
