package net.lodoma.lime.physics;

import java.nio.ByteBuffer;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

public class PhysicsBody
{
    private static final int INDEX_ID = 0;
    private static final int SIZE_ID = 8;
    private static final int INDEX_POSX = INDEX_ID + SIZE_ID;
    private static final int SIZE_POSX = 4;
    private static final int INDEX_POSY = INDEX_POSX + SIZE_POSX;
    private static final int SIZE_POSY = 4;
    private static final int INDEX_TYPE = INDEX_POSY + SIZE_POSY;
    private static final int SIZE_TYPE = 1;
    private static final int INDEX_SHAPE = INDEX_TYPE + SIZE_TYPE;
    private static final int SIZE_SHAPE = 1;
    private static final int INDEX_DENSITY = INDEX_SHAPE + SIZE_SHAPE;
    private static final int SIZE_DENSITY = 4;
    private static final int INDEX_FRICTION = INDEX_DENSITY + SIZE_DENSITY;
    private static final int SIZE_FRICTION = 4;
    private static final int INDEX_RESTITUTION = INDEX_FRICTION + SIZE_FRICTION;
    private static final int SIZE_RESTITUTION = 4;
    private static final int INDEX_SHAPEARG1 = INDEX_RESTITUTION + SIZE_RESTITUTION;
    private static final int SIZE_SHAPEARG = 4;
    private static final int INDEX_SHAPEARG2 = INDEX_SHAPEARG1 + SIZE_SHAPEARG;
    private static final int SIZE_SHAPEARGMAX = 32;
    
    private static final int SHAPE_CIRCLE = 0;
    private static final int SHAPE_POLYGON = 1;
    
    /*
     * The counterID being a 64-bit integer sets the maximum amount
     * of bodies loaded at once to pow(2, 64);
     */
    private static long counterID = 0;
    private long ID;
    
    private BodyDef bd;
    private Shape shape;
    private FixtureDef fd;
    
    private Fixture fixture;
    private Body body;
    
    private ByteBuffer bytes;
    
    public PhysicsBody()
    {
        bd = new BodyDef();
        fd = new FixtureDef();
    }
    
    public long getID()
    {
        return ID;
    }
    
    public void generateID()
    {
        bytes.putLong(INDEX_ID, counterID++);
    }
    
    public void setPosition(Vector2 pos)
    {
        bytes.putFloat(INDEX_POSX, pos.x);
        bytes.putFloat(INDEX_POSY, pos.y);
    }
    
    public void setBodyType(PhysicsBodyType type)
    {
        bytes.put(INDEX_TYPE, (byte) type.ordinal());
    }
    
    private void setShape(int shapeType)
    {
        bytes.put(INDEX_SHAPE, (byte) shapeType);
    }
    
    public void setCircleShape(float radius)
    {
        setShape(SHAPE_CIRCLE);
        bytes.putFloat(INDEX_SHAPEARG1, radius);
    }
    
    public void setPolygonShape(Vector2... vertices)
    {
        setShape(SHAPE_POLYGON);
        bytes.putInt(INDEX_SHAPEARG1, vertices.length);
        for(int i = 0; i < vertices.length; i++)
        {
            bytes.putFloat(INDEX_SHAPEARG2 + SIZE_SHAPEARG * (i * 2 + 0), vertices[i].x);
            bytes.putFloat(INDEX_SHAPEARG2 + SIZE_SHAPEARG * (i * 2 + 1), vertices[i].y);
        }
    }
    
    public void setDensity(float density)
    {
        bytes.putFloat(INDEX_DENSITY, density);
    }
    
    public void setFriction(float friction)
    {
        bytes.putFloat(INDEX_FRICTION, friction);
    }
    
    public void setRestitution(float restitution)
    {
        bytes.putFloat(INDEX_RESTITUTION, restitution);
    }
    
    public byte[] getBytes()
    {
        return bytes.array();
    }
    
    public void setBytes(byte[] lbytes)
    {
        bytes.rewind();
        bytes.put(lbytes);
    }
    
    public void reload()
    {
        long id = bytes.getLong(INDEX_ID);
        
        float posX = bytes.getFloat(INDEX_POSX);
        float posY = bytes.getFloat(INDEX_POSY);
        
        PhysicsBodyType type = PhysicsBodyType.values()[bytes.get(INDEX_TYPE)];
        byte shapeType = bytes.get(INDEX_SHAPE);

        float density = bytes.getFloat(INDEX_DENSITY);
        float friction = bytes.getFloat(INDEX_FRICTION);
        float restitution = bytes.getFloat(INDEX_RESTITUTION);
        
        ID = id;
        
        bd.position.set(posX, posY);
        bd.type = type.getEngineValue();
        
        Shape shape = null;
        switch(shapeType)
        {
        case SHAPE_CIRCLE:
            shape = new CircleShape();
            shape.m_radius = bytes.getFloat(INDEX_SHAPEARG1);
            break;
        case SHAPE_POLYGON:
            shape = new PolygonShape();
            int vertexC = bytes.getInt(INDEX_SHAPEARG1);
            Vec2[] vertices = new Vec2[vertexC];
            for(int i = 0; i < vertexC; i++)
            {
                float vertexX = bytes.getFloat(INDEX_SHAPEARG2 + SIZE_SHAPEARG * (i * 2 + 0));
                float vertexY = bytes.getFloat(INDEX_SHAPEARG2 + SIZE_SHAPEARG * (i * 2 + 1));
                vertices[i] = new Vec2(vertexX, vertexY);
            }
            ((PolygonShape) shape).set(vertices, vertexC);
            break;
        }
        this.shape = shape;
        fd.shape = this.shape;
        
        fd.density = density;
        fd.friction = friction;
        fd.restitution = restitution;
    }
    
    public void create(PhysicsWorld world)
    {
        body = world.getEngineWorld().createBody(bd);
        fixture = body.createFixture(fd);
    }
    
    public void destroy(PhysicsWorld world)
    {
        body.destroyFixture(fixture);
        world.getEngineWorld().destroyBody(body);
    }
}
