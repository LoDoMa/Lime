package net.lodoma.lime.server.io.base;

import java.io.IOException;

import net.lodoma.lime.common.NetStage;
import net.lodoma.lime.event.EventBundle;
import net.lodoma.lime.event.EventManager;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.HashHelper;
import net.lodoma.lime.util.HashPool32;

public class SPNetworkStageChange extends ServerPacket
{
    public static final String NAME = "Lime::NetworkStageChange";
    public static final int HASH = HashHelper.hash32(NAME);
    
    private EventManager manager;

    @SuppressWarnings("unchecked")
    public SPNetworkStageChange(Server server)
    {
        super(server, HASH, NetStage.class);
        HashPool32<EventManager> emanPool = (HashPool32<EventManager>) server.getProperty("emanPool");
        manager = emanPool.get(EventManager.ON_NEW_USER_HASH);
    }
    
    @Override
    protected void localWrite(ServerUser user, Object... args) throws IOException
    {
        NetStage stage = (NetStage) args[0];
        user.stage = stage;
        user.outputStream.writeInt(stage.ordinal());
        if(stage == NetStage.USER)
            manager.onEvent(new EventBundle(new String[] {"userID"}, new Object[] {user.getID()}));
    }
}
