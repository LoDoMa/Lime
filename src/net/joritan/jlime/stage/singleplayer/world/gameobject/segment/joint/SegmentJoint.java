package net.joritan.jlime.stage.singleplayer.world.gameobject.segment.joint;

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
        if(!(joint instanceof RevoluteJoint))
            throw new UnsupportedOperationException();
        return ((RevoluteJoint) joint).getMotorSpeed();
    }

    public void setMotorSpeed(float motorSpeed)
    {
        if(!(joint instanceof RevoluteJoint))
            throw new UnsupportedOperationException();
        ((RevoluteJoint) joint).setMotorSpeed(motorSpeed);
    }
}
