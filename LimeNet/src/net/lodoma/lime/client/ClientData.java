package net.lodoma.lime.client;

import net.lodoma.lime.chat.ChatManager;
import net.lodoma.lime.client.generic.net.packet.ClientPacketPool;
import net.lodoma.lime.common.net.LogicPool;
import net.lodoma.lime.common.net.NetStage;
import net.lodoma.lime.mod.ModulePool;

public final class ClientData
{
    public ClientPacketPool packetPool;
    public ModulePool modulePool;
    public LogicPool logicPool;
    
    public NetStage networkStage;
    public long lastServerResponse;
    
    public ChatManager chatManager;
}
