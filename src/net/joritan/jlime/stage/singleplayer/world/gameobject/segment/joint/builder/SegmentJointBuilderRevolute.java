package net.joritan.jlime.stage.singleplayer.world.gameobject.segment.joint.builder;

import net.joritan.jlime.util.Vector2;
import net.joritan.jlime.stage.singleplayer.world.Environment;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.Segment;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.joint.SegmentJoint;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class SegmentJointBuilderRevolute implements SegmentJointBuilder
{
    @Override
    public SegmentJoint buildSegmentJoint(Environment environment, Segment segmentA, Segment segmentB, Object... args)
    {
        if(args.length >= 2)
        {
            if(!(args[0] instanceof Vector2)) throw new IllegalArgumentException();
            if(!(args[1] instanceof Vector2)) throw new IllegalArgumentException();
            Vector2 anchorA = (Vector2) args[0];
            Vector2 anchorB = (Vector2) args[1];
            RevoluteJointDef def = new RevoluteJointDef();
            def.bodyA = segmentA.getBody();
            def.bodyB = segmentB.getBody();
            def.collideConnected = false;
            def.localAnchorA.set(anchorA.x, anchorA.y);
            def.localAnchorB.set(anchorB.x, anchorB.y);
            if(args.length == 3)
            {
                if(!(args[2] instanceof Float)) throw new IllegalArgumentException();
                float maxTorque = (Float) args[2];
                def.enableMotor = true;
                def.maxMotorTorque = maxTorque;
            }
            else
            if(args.length > 3) throw new IllegalArgumentException();
            Joint revolute = environment.world.createJoint(def);
            return new SegmentJoint(revolute);
        }
        else throw new IllegalArgumentException();
    }
}
