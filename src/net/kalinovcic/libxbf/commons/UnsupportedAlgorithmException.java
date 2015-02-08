package net.kalinovcic.libxbf.commons;

public class UnsupportedAlgorithmException extends RuntimeException
{
    private static final long serialVersionUID = -1950197399590835600L;

    public UnsupportedAlgorithmException()
    {
        super();
    }
    
    public UnsupportedAlgorithmException(String message)
    {
        super(message);
    }
}
