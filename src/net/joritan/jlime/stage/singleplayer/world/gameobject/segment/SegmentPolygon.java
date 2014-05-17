package net.joritan.jlime.stage.singleplayer.world.gameobject.segment;

import net.joritan.jlime.util.Vector2;
import net.joritan.jlime.stage.singleplayer.world.Environment;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import static org.lwjgl.opengl.GL11.*;

public class SegmentPolygon extends Segment
{
    private BodyDef bodyDef;
    private PolygonShape shape;
    private FixtureDef fixture;

    public SegmentPolygon(Environment environment, boolean isDyanmic, Object... args)
    {
        if(args.length == 4)
        {
            if(!(args[0] instanceof Vector2[])) throw new IllegalArgumentException();
            if(!(args[1] instanceof Float)) throw new IllegalArgumentException();
            if(!(args[2] instanceof Float)) throw new IllegalArgumentException();
            if(!(args[3] instanceof Float)) throw new IllegalArgumentException();
            Vector2[] vertices = (Vector2[]) args[0];
            float density = (Float) args[1];
            float friction = (Float) args[2];
            float restitution = (Float) args[3];

            Vec2[] jvertices = new Vec2[vertices.length];
            for(int i = 0; i < vertices.length; i++)
                jvertices[i] = new Vec2(vertices[i].x, vertices[i].y);

            bodyDef = new BodyDef();
            bodyDef.type = isDyanmic ? BodyType.DYNAMIC : BodyType.KINEMATIC;
            bodyDef.position.set(0, 0);

            shape = new PolygonShape();
            shape.set(jvertices, jvertices.length);

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

        glBegin(GL_LINE_LOOP);
        Vec2[] vertices = shape.getVertices();
        for(Vec2 vertex : vertices)
        {
            glVertex2f(vertex.x, vertex.y);
        }
        glEnd();

        glPopMatrix();
    }
}
