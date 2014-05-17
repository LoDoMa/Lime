package net.joritan.jlime.stage.singleplayer.world.gameobject.segment;

import net.joritan.jlime.util.RenderUtil;
import net.joritan.jlime.stage.singleplayer.world.Environment;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class SegmentCircle extends Segment
{
    private BodyDef bodyDef;
    private CircleShape shape;
    private FixtureDef fixture;

    public SegmentCircle(Environment environment, boolean isDynamic, Object... args)
    {
        if(args.length == 4)
        {
            if(!(args[0] instanceof Float)) throw new IllegalArgumentException();
            if(!(args[1] instanceof Float)) throw new IllegalArgumentException();
            if(!(args[2] instanceof Float)) throw new IllegalArgumentException();
            if(!(args[3] instanceof Float)) throw new IllegalArgumentException();
            float radius = (Float) args[0];
            float density = (Float) args[1];
            float friction = (Float) args[2];
            float restitution = (Float) args[3];

            bodyDef = new BodyDef();
            bodyDef.type = isDynamic ? BodyType.DYNAMIC : BodyType.KINEMATIC;
            bodyDef.position.set(0, 0);

            shape = new CircleShape();
            shape.setRadius(radius);

            fixture = new FixtureDef();
            fixture.shape = shape;
            fixture.density = density;
            fixture.friction = friction;
            fixture.restitution = restitution;

            body = environment.world.createBody(bodyDef);
            body.createFixture(fixture);
        }
        else
            throw new IllegalArgumentException();
    }

    @Override
    public void update(float timeDelta)
    {

    }

    @Override
    public void render()
    {
        Vec2 pos = body.getPosition();
        float angle = body.getAngle();

        glPushMatrix();
        glTranslatef(pos.x, pos.y, 0.0f);
        glRotatef((float) Math.toDegrees(angle), 0, 0, 1);

        RenderUtil.drawCircle(0, 0, shape.getRadius(), 10);

        glPopMatrix();
    }
}
