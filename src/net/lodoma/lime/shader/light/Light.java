package net.lodoma.lime.shader.light;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.shader.Program;
import net.lodoma.lime.shader.UniformType;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.Vector2;
import net.lodoma.lime.world.World;
import net.lodoma.lime.world.physics.PhysicsComponentSnapshot;

public class Light implements Identifiable<Integer>
{
    public int identifier;
    
    public World world;
    public LightData data;
    
    public Light(World world)
    {
        this.world = world;
        data = new LightData();
    }
    
    @Override
    public Integer getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public void setIdentifier(Integer identifier)
    {
        this.identifier = identifier;
    }
    
    public void render()
    {
        Program.lightProgram.useProgram();
        
        Program.lightProgram.setUniform("color", UniformType.FLOAT3, data.color.getR(), data.color.getG(), data.color.getB());
        Program.lightProgram.setUniform("radius", UniformType.FLOAT1, data.radius);
        Program.lightProgram.setUniform("center", UniformType.FLOAT2, data.position.x, data.position.y);
        
        glBegin(GL_QUADS);
        glVertex2f(data.position.x - data.radius, data.position.y - data.radius);
        glVertex2f(data.position.x + data.radius, data.position.y - data.radius);
        glVertex2f(data.position.x + data.radius, data.position.y + data.radius);
        glVertex2f(data.position.x - data.radius, data.position.y + data.radius);
        glEnd();
    }
    
    public void renderShadows()
    {
        Program.lightProgram.useProgram();
        
        Program.lightProgram.setUniform("color", UniformType.FLOAT4, data.color.getR(), data.color.getG(), data.color.getB(), data.color.getA());
        Program.lightProgram.setUniform("radius", UniformType.FLOAT1, data.radius);
        Program.lightProgram.setUniform("center", UniformType.FLOAT2, data.position.x, data.position.y);
        
        List<Vector2> lightPolygon = new ArrayList<Vector2>();
        
        /*
        if (world.compoSnapshotPool != null)
        {
            world.compoSnapshotPool.foreach((PhysicsComponentSnapshot snapshot) -> {
                switch (snapshot.type)
                {
                case CIRCLE:
                    for (int i = 0; i <= 10; i++)
                    {
                        float cangle = snapshot.angle + (float) Math.toRadians(i * 360.0 / 10.0);
                        float x = (float) Math.cos(cangle) * snapshot.radius;
                        float y = (float) Math.sin(cangle) * snapshot.radius;
                        float nx = snapshot.position.x + x;
                        float ny = snapshot.position.y + y;
                        Vector2 v = new Vector2(nx, ny);
                        lightPolygon.add(raycast(v));
                        v.rotateLocal(0.0001f);
                        lightPolygon.add(raycast(v));
                        v.rotateLocal(-2 * 0.0001f);
                        lightPolygon.add(raycast(v));
                    }
                    break;
                case POLYGON:
                    for (int i = 0; i < snapshot.vertices.length; i++)
                    {
                        Vector2 v = snapshot.vertices[i].rotate(snapshot.angle);
                        v.addLocal(snapshot.position);
                        lightPolygon.add(raycast(v));
                        v.rotateLocal(0.0001f);
                        lightPolygon.add(raycast(v));
                        v.rotateLocal(-2 * 0.0001f);
                        lightPolygon.add(raycast(v));
                    }
                    break;
                }
            });
        }
        
        lightPolygon.add(raycast(new Vector2(data.position.x - data.radius, data.position.y - data.radius)));
        lightPolygon.add(raycast(new Vector2(data.position.x + data.radius, data.position.y - data.radius)));
        lightPolygon.add(raycast(new Vector2(data.position.x + data.radius, data.position.y + data.radius)));
        lightPolygon.add(raycast(new Vector2(data.position.x - data.radius, data.position.y + data.radius)));
        */
        
        for (int i = 0; i < 180; i++)
        {
            Vector2 d = new Vector2(0.0f, 1.0f);
            d.rotateLocalDeg((360.0f / 180.0f) * i);
            d.addLocal(data.position);
            lightPolygon.add(raycast(d));
        }
        
        lightPolygon.sort((Vector2 v1, Vector2 v2) ->
        {
            double a1 = v1.sub(data.position).angle();
            double a2 = v2.sub(data.position).angle();
            return a1 < a2 ? -1 : a1 > a2 ? 1 : 0;
        });
        
        for (int i = 0; i < lightPolygon.size(); i++)
        {
            Vector2 v1 = lightPolygon.get(i);
            Vector2 v2 = lightPolygon.get((i + 1) % lightPolygon.size());
            glBegin(GL_TRIANGLES);
            glVertex2f(data.position.x, data.position.y);
            glVertex2f(v1.x, v1.y);
            glVertex2f(v2.x, v2.y);
            glEnd();
        }
    }

    private Vector2 raycast(Vector2 target)
    {
        Vector2 edgeA = new Vector2(data.position.x - data.radius, data.position.y - data.radius);
        Vector2 edgeB = new Vector2(data.position.x + data.radius, data.position.y - data.radius);
        Vector2 edgeC = new Vector2(data.position.x + data.radius, data.position.y + data.radius);
        Vector2 edgeD = new Vector2(data.position.x - data.radius, data.position.y + data.radius);

        Vector2 best = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
        
        if (world.compoSnapshotPool != null)
        {
            world.compoSnapshotPool.foreach((PhysicsComponentSnapshot snapshot) -> {
                switch (snapshot.type)
                {
                case CIRCLE:
                    for (int i = 0; i < 10; i++)
                    {
                        float cangle = snapshot.angle + (float) Math.toRadians(i * 360.0 / 10.0);
                        float cangle2 = snapshot.angle + (float) Math.toRadians(((i + 1) % 10) * 360.0 / 10.0);
                        float x = (float) Math.cos(cangle) * snapshot.radius;
                        float y = (float) Math.sin(cangle) * snapshot.radius;
                        float x2 = (float) Math.cos(cangle2) * snapshot.radius;
                        float y2 = (float) Math.sin(cangle2) * snapshot.radius;
                        tryReplace(new Vector2(snapshot.position.x + x, snapshot.position.y + y),
                                   new Vector2(snapshot.position.x + x2, snapshot.position.y + y2),
                                   target, best); 
                    }
                    break;
                case POLYGON:
                    for (int i = 0; i < snapshot.vertices.length; i++)
                    {
                        Vector2 v = snapshot.vertices[i].rotate(snapshot.angle);
                        Vector2 v2 = snapshot.vertices[(i + 1) % snapshot.vertices.length].rotate(snapshot.angle);
                        v.addLocal(snapshot.position);
                        v2.addLocal(snapshot.position);
                        tryReplace(v, v2, target, best);
                    }
                    break;
                }
            });
        }
        
        tryReplace(edgeA, edgeB, target, best);
        tryReplace(edgeB, edgeC, target, best);
        tryReplace(edgeC, edgeD, target, best);
        tryReplace(edgeD, edgeA, target, best);
        
        return best;
    }
    
    private void tryReplace(Vector2 a, Vector2 b, Vector2 target, Vector2 best)
    {
        Vector2 cand = intersect(a, b, data.position, target);
        if (cand == null) return;
        if (cand.dist(data.position) < best.dist(data.position))
            best.set(cand);
    }
    
    private Vector2 intersect(Vector2 a, Vector2 b, Vector2 c, Vector2 d)
    {
        boolean v1 = compareDoubles(a.x - b.x, 0.0);
        boolean v2 = compareDoubles(c.x - d.x, 0.0);
        
        if (v1 && v2) return null;

        double k1 = 0;
        double k2 = 0;
        double l1 = 0;
        double l2 = 0;
        
        if (!v1)
        {
            k1 = (a.y - b.y) / (a.x - b.x);
            l1 = a.y - k1 * a.x;
        }
        
        if (!v2)
        {
            k2 = (c.y - d.y) / (c.x - d.x);
            l2 = c.y - k2 * c.x;
        }
        
        double x = 0;
        double y = 0;
        
        if (!v1 && !v2)
        {
            x = (l2 - l1) / (k1 - k2);
            y = k1 * x + l1;
        }
        
        if (v1)
        {
            x = a.x;
            y = k2 * x + l2; 
        }
        
        if (v2)
        {
            x = c.x;
            y = k1 * x + l1;
        }
        
        if (x < Math.min(a.x, b.x) || x > Math.max(a.x, b.x)) return null;
        if (y < Math.min(a.y, b.y) || y > Math.max(a.y, b.y)) return null;
        if ((d.x < c.x && x >= c.x) || (d.x > c.x && x <= c.x)) return null;
        if ((d.y < c.y && y >= c.y) || (d.y > c.y && y <= c.y)) return null;
        
        return new Vector2((float) x, (float) y);
    }
    
    private boolean compareDoubles(double a, double b)
    {
        double diff = Math.abs(a - b);
        return diff < 0.000001;
    }
}
