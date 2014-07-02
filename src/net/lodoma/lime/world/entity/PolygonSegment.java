package net.lodoma.lime.world.entity;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class PolygonSegment extends BodySegment
{
    private Body body;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;
    private PolygonShape shape;
    
    private Vector2[] vertices;
    private Vec2[] verticesPhysics;
    
    public PolygonSegment(Vector2[] vertices)
    {
        this.vertices = vertices;
        this.verticesPhysics = Vector2.toVec2Array(this.vertices);
        if(vertices.length > Settings.maxPolygonVertices)
            throw new PolygonVertexCountException(vertices.length + ">" + Settings.maxPolygonVertices);
    }
    
    @Override
    protected void construct()
    {
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.userData = this;
        
        World world = entity.getWorld();
        body = new Body(bodyDef, world);
        
        shape = new PolygonShape();
        shape.set(verticesPhysics, verticesPhysics.length);
        
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        
        body.m_fixtureList = body.createFixture(fixtureDef);
    }
}
