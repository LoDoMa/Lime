package net.kalinovcic.libxbf.commons;

public class UnsupportedVersionException extends RuntimeException
{
    private static final long serialVersionUID = -5918036148123325877L;

    public UnsupportedVersionException()
    {
        super();
    }
    
    public UnsupportedVersionException(String message)
    {
        super(message);
    }
}
