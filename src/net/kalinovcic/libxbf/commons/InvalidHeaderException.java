package net.kalinovcic.libxbf.commons;

public class InvalidHeaderException extends RuntimeException
{
    private static final long serialVersionUID = -3173836816445952581L;

    public InvalidHeaderException()
    {
        super();
    }
    
    public InvalidHeaderException(String message)
    {
        super(message);
    }
}
