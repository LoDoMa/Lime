package net.lodoma.lime.server.io.base;

import java.io.File;
import java.io.IOException;

import net.lodoma.lime.security.ModificationCheck;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerOutput;
import net.lodoma.lime.server.ServerUser;
import net.lodoma.lime.util.HashHelper;

public class SOModificationCheck extends ServerOutput
{
    public static final String NAME = "Lime::ModificationCheck";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public SOModificationCheck(Server server)
    {
        super(server, HASH);
    }
    
    @Override
    protected void localHandle(ServerUser user, Object... args) throws IOException
    {
        if(!server.hasProperty("crcsum-script"))
            server.setProperty("crcsum-script", ModificationCheck.sumCRC(new File("./script")));
        if(!server.hasProperty("crcsum-model"))
            server.setProperty("crcsum-model", ModificationCheck.sumCRC(new File("./model")));

        user.outputStream.writeLong((Long) server.getProperty("crcsum-script"));
        user.outputStream.writeLong((Long) server.getProperty("crcsum-model"));
    }
}
