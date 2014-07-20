package net.lodoma.lime.client;

public class ClientConnectionException extends Exception
{
    private static final long serialVersionUID = -5878384965076974066L;

    public ClientConnectionException(Exception e)
    {
        super(e);
    }
}
