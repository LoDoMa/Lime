package net.joritan.jlime.util;

/**
 * Created by Lovro on 29.3.2014..
 */
public class LimeException extends RuntimeException
{
    private static final long serialVersionUID = 5106627315161995387L;

    public LimeException()
    {

    }

    public LimeException(String msg)
    {
        super(msg);
    }
}
