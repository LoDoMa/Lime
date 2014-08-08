package net.lodoma.lime.physics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

import net.lodoma.lime.util.Vector2;

public class PhysicsBodyDescription
{
    private Shape shape;
    
    BodyDef bodyDef;
    FixtureDef fixtureDef;
    
    public PhysicsBodyDescription(PhysicsBodyData data)
    {
        bodyDef = new BodyDef();
        bodyDef.position = data.position.toVec2();
        bodyDef.angle = data.angle;
        bodyDef.type = data.bodyType.getEngineValue();
        
        switch(data.shapeType)
        {
        case CIRCLE:
            shape = new CircleShape();
            ((CircleShape) shape).m_radius = data.shapeRadius;
            break;
        case POLYGON:
            shape = new PolygonShape();
            ((PolygonShape) shape).set(Vector2.toVec2Array(data.shapeVertices), data.shapeVertices.length);
            break;
        }
        
        fixtureDef = new FixtureDef();
        fixtureDef.density = data.density;
        fixtureDef.friction = data.friction;
        fixtureDef.restitution = data.restitution;
        fixtureDef.shape = shape;
    }
}
