package net.lodoma.lime.physics;

import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.jbox2d.dynamics.joints.FrictionJointDef;
import org.jbox2d.dynamics.joints.GearJointDef;
import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.JointType;
import org.jbox2d.dynamics.joints.LineJointDef;
import org.jbox2d.dynamics.joints.MouseJointDef;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.jbox2d.dynamics.joints.PulleyJointDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.WeldJointDef;

public enum PhysicsJointType
{
    REVOLUTE (JointType.REVOLUTE,  new Builder() {public JointDef jointDef() {return new RevoluteJointDef();}}),
    DISTANCE (JointType.DISTANCE,  new Builder() {public JointDef jointDef() {return new DistanceJointDef();}}),
    PRISMATIC(JointType.PRISMATIC, new Builder() {public JointDef jointDef() {return new PrismaticJointDef();}}),
    LINE     (JointType.LINE,      new Builder() {public JointDef jointDef() {return new LineJointDef();}}),
    WELD     (JointType.WELD,      new Builder() {public JointDef jointDef() {return new WeldJointDef();}}),
    PULLEY   (JointType.PULLEY,    new Builder() {public JointDef jointDef() {return new PulleyJointDef();}}),
    FRICTION (JointType.FRICTION,  new Builder() {public JointDef jointDef() {return new FrictionJointDef();}}),
    GEAR     (JointType.GEAR,      new Builder() {public JointDef jointDef() {return new GearJointDef();}}),
    MOUSE    (JointType.MOUSE,     new Builder() {public JointDef jointDef() {return new MouseJointDef();}});
    
    private interface Builder
    {
        public JointDef jointDef();
    }
    
    private JointType engineType;
    private Builder builder;
    
    private PhysicsJointType(JointType engineType, Builder builder)
    {
        this.engineType = engineType;
        this.builder = builder;
    }
    
    public JointType getEngineType()
    {
        return engineType;
    }
    
    public JointDef jointDef()
    {
        return builder.jointDef();
    }
}