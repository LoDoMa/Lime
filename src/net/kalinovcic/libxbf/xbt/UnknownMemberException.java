package net.kalinovcic.libxbf.xbt;

public class UnknownMemberException extends RuntimeException
{
    private static final long serialVersionUID = -2373717734806440086L;

    public UnknownMemberException()
    {
        super();
    }
    
    public UnknownMemberException(String message)
    {
        super(message);
    }
}
