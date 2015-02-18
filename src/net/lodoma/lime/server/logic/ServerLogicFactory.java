package net.lodoma.lime.server.logic;

import net.lodoma.lime.server.Server;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.IdentityPool;

public abstract class ServerLogicFactory implements Identifiable<Integer>
{
    public static final IdentityPool<ServerLogicFactory> factoryPool = new IdentityPool<ServerLogicFactory>(true);
    static
    {
        ServerLogicFactory.factoryPool.add(new ServerLogicFactory(SLGame.HASH)
        {
            @Override
            public ServerLogic newInstance(Server server)
            {
                return new SLGame(server);
            }
        });
    }
    
    private final int hash;
    
    public ServerLogicFactory(int hash)
    {
        this.hash = hash;
    }
    
    @Override
    public Integer getIdentifier()
    {
        return hash;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        throw new UnsupportedOperationException();
    }
    
    public abstract ServerLogic newInstance(Server server);
}
