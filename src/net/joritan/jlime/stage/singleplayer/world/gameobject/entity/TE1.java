package net.joritan.jlime.stage.singleplayer.world.gameobject.entity;

import net.joritan.jlime.util.Input;
import net.joritan.jlime.util.Texture;
import net.joritan.jlime.util.Vector2;
import net.joritan.jlime.stage.singleplayer.world.Camera;
import net.joritan.jlime.stage.singleplayer.world.Environment;
import net.joritan.jlime.stage.singleplayer.world.gameobject.mask.Mask;
import net.joritan.jlime.stage.singleplayer.world.gameobject.mask.SegmentMaskBinding;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.SegmentType;
import net.joritan.jlime.stage.singleplayer.world.gameobject.segment.joint.SegmentJointType;

public class TE1 extends Entity
{
    public TE1(Environment environment)
    {
        super(environment);

        addSegment("polygon", SegmentType.POLYGON, new Vector2[]
                {
                    new Vector2(0, 0),
                    new Vector2(0, 1),
                    new Vector2(1, 1),
                    new Vector2(1, 0)
                }, 0.09f, 0.3f, 0.0f);
        addSegment("circle", SegmentType.CIRCLE, 0.5f, 0.09f, 0.3f, 0.0f);
        addSegmentJoint("motor", "polygon", "circle", SegmentJointType.REVOLUTE,
                new Vector2(0.5f, 0.0f), new Vector2(0.0f, 0.0f), 100.0f);

        getSegment("polygon").setLinearDamping(0.3f);
        getSegment("circle").setLinearDamping(0.3f);

        getSegment("polygon").setTransform(new Vector2(0.0f, 0.0f), 0.0f);
        getSegment("polygon").setFixedRotation(true);

        Mask mask1 = new Mask(new SegmentMaskBinding(getSegment("polygon")), Texture.getTexture("dirt"),
            new Vector2[]
            {
                new Vector2(0, 0),
                new Vector2(0, 1),
                new Vector2(1, 1),
                new Vector2(1, 0)
            });
        environment.addGameObject(mask1);

        Mask mask2 = new Mask(new SegmentMaskBinding(getSegment("circle")), Texture.getTexture("dirt"),
            new Vector2[]
            {
                new Vector2(-0.5f, -0.5f),
                new Vector2(-0.5f, +0.5f),
                new Vector2(+0.5f, +0.5f),
                new Vector2(+0.5f, -0.5f)
            });
        ((SegmentMaskBinding) mask2.getBinding()).setStaticPos(new Vector2(0.0f, 0.0f));
        environment.addGameObject(mask2);
    }

    @Override
    public void update(float timeDelta)
    {
        super.update(timeDelta);
        getSegmentJoint("motor").setMotorSpeed(0);

        if(Input.getKey(Input.KEY_LSHIFT))
        {
            if(Input.getKeyDown(Input.KEY_A))
                getSegment("polygon").setLinearVelocity(
                    getSegment("polygon").getLinearVelocity().add(new Vector2(-50.0f, 0.0f)));
            if(Input.getKeyDown(Input.KEY_D))
                getSegment("polygon").setLinearVelocity(
                    getSegment("polygon").getLinearVelocity().add(new Vector2(50.0f, 0.0f)));
        }
        else
        {
            if(Input.getKey(Input.KEY_LCONTROL))
            {
                if(Input.getKeyDown(Input.KEY_A))
                    getSegment("polygon").setLinearVelocity(
                            getSegment("polygon").getLinearVelocity().add(new Vector2(-5f, 0.0f)));
                if(Input.getKeyDown(Input.KEY_D))
                    getSegment("polygon").setLinearVelocity(
                            getSegment("polygon").getLinearVelocity().add(new Vector2(5f, 0.0f)));
            }
            else
            {
                if (Input.getKey(Input.KEY_A))
                    getSegmentJoint("motor").setMotorSpeed(15);
                if (Input.getKey(Input.KEY_D))
                    getSegmentJoint("motor").setMotorSpeed(-15);
            }
        }

        if(Input.getKeyDown(Input.KEY_W))
            getSegment("polygon").setLinearVelocity(getSegment("polygon").getLinearVelocity().add(new Vector2(0.0f, 20.0f)));
        if(Input.getKeyDown(Input.KEY_S))
            getSegment("polygon").setLinearVelocity(getSegment("polygon").getLinearVelocity().add(new Vector2(0.0f, -50.0f)));

        if(Input.getKeyDown(Input.KEY_SPACE))
        {
            getSegment("polygon").setLinearVelocity(new Vector2(0.0f, 0.0f));
            getSegment("polygon").setAngularVelocity(0.0f);
            getSegment("polygon").setTransform(new Vector2(0.0f, 0.0f), 0.0f);
        }

        Vector2 bodyPosition = getSegment("circle").getPosition();
        Vector2 cornerUpLeft = bodyPosition.add(new Vector2(-10.0f, -10.0f));
        Vector2 cornerDownRight = bodyPosition.add(new Vector2(10.0f, 10.0f));

        Camera camera = new Camera(cornerUpLeft.x, cornerUpLeft.y,
                                   cornerDownRight.x, cornerDownRight.y);
        environment.camera = camera;
    }
}
