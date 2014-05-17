package net.joritan.jlime.stage.singleplayer.world.gameobject.segment.joint;

import net.joritan.jlime.stage.root.BlueScreen;

import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;

public class SegmentJoint
{
    private Joint joint;
    
    public SegmentJoint(Joint joint)
    {
        this.joint = joint;
    }
    
    public Joint getJoint()
    {
        return joint;
    }
    
    public float getMotorSpeed()
    {
        if (!(joint instanceof RevoluteJoint))
        {
            new BlueScreen(null, new UnsupportedOperationException("unsupported operation"));
            return 0.0f;
        }
        return ((RevoluteJoint) joint).getMotorSpeed();
    }
    
    public void setMotorSpeed(float motorSpeed)
    {
        if (!(joint instanceof RevoluteJoint))
        {
            new BlueScreen(null, new UnsupportedOperationException("unsupported operation"));
            return;
        }
        ((RevoluteJoint) joint).setMotorSpeed(motorSpeed);
    }
}
