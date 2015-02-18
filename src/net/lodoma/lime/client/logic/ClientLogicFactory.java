package net.lodoma.lime.client.logic;

import net.lodoma.lime.client.Client;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.IdentityPool;

public abstract class ClientLogicFactory implements Identifiable<Integer>
{
    public static final IdentityPool<ClientLogicFactory> factoryPool = new IdentityPool<ClientLogicFactory>(true);
    static
    {
        ClientLogicFactory.factoryPool.add(new ClientLogicFactory(CLGame.HASH)
        {
            @Override
            public ClientLogic newInstance(Client client)
            {
                return new CLGame(client);
            }
        });
    }
    
    private final int hash;
    
    public ClientLogicFactory(int hash)
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
    
    public abstract ClientLogic newInstance(Client client);
}
