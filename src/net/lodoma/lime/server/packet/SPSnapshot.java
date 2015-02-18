package net.lodoma.lime.server.packet;

import java.io.IOException;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerPacket;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.snapshot.Snapshot;
import net.lodoma.lime.util.HashHelper;

public class SPSnapshot extends ServerPacket
{
    public static final String NAME = "Lime::Snapshot";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SPSnapshot(Server server)
    {
        super(server, HASH, Snapshot.class);
    }
    
    @Override
    protected void localWrite(ServerUser user, Object... args) throws IOException
    {
        Snapshot snapshot = (Snapshot) args[0];
        
        user.outputStream.writeInt(snapshot.logicID);
        user.outputStream.writeInt(snapshot.snapshotID);
        snapshot.data.write(server, user);
    }
}