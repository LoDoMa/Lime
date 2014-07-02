package net.lodoma.lime.world.entity;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;

public class PolygonSegment extends BodySegment
{
    private Vector2[] vertices;
    private Vec2[] verticesPhysics;
    
    public PolygonSegment(Vector2[] vertices)
    {
        this.vertices = vertices;
        this.verticesPhysics = Vector2.toVec2Array(this.vertices);
        assert verticesPhysics.length <= Settings.maxPolygonVertices;
    }
    
    @Override
    protected void construct()
    {
        
    }
    
    @Override
    protected void destroy()
    {
        
    }
}
