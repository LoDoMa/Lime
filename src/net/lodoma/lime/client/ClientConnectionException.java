package net.lodoma.lime.client;

/**
 * ClientConnectionException is thrown by Client on failed opening.
 * 
 * @author Lovro Kalinovčić
 */
public class ClientConnectionException extends Exception
{
    private static final long serialVersionUID = -5878384965076974066L;

    /**
     * @param e - cause
     */
    public ClientConnectionException(Exception e)
    {
        super(e);
    }
}
