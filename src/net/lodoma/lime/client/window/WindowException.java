package net.lodoma.lime.client.window;

public class WindowException extends Exception
{
    private static final long serialVersionUID = -6475041927468452704L;
    
    public WindowException(String msg)
    {
        super(msg);
    }
    
    public WindowException(Throwable cause)
    {
        super(cause);
    }
}
