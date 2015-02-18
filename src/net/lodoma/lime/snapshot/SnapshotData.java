package net.lodoma.lime.snapshot;

import java.io.DataInputStream;
import java.io.IOException;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.server.Server;
import net.lodoma.lime.server.ServerUser;

public interface SnapshotData
{
    public void read(Client client, DataInputStream in) throws IOException;
    public void write(Server server, ServerUser user) throws IOException;
}
