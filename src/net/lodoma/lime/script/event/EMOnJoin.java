package net.lodoma.lime.script.event;

import net.lodoma.lime.util.HashHelper;

public class EMOnJoin extends EventManager
{
    public static final String NAME = "Lime::OnJoin";
    public static final int HASH = HashHelper.hash32(NAME);
    
    public EMOnJoin()
    {
        super(HASH);
    }
}
