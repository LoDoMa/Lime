package net.lodoma.lime.script.event;

import net.lodoma.lime.util.HashHelper;

public class EMOnLeave extends EventManager
{
    public static final String NAME = "Lime::OnLeave";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public EMOnLeave()
    {
        super(HASH);
    }
}
