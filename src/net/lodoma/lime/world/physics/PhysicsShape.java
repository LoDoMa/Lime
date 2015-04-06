package net.lodoma.lime.world.physics;

import java.util.ArrayList;
import java.util.List;

import net.lodoma.lime.util.Color;
import net.lodoma.lime.util.Identifiable;
import net.lodoma.lime.util.Vector2;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

public abstract class PhysicsShape implements Identifiable<Integer>
{
    public int identifier;
    
    // Physical data
    public final Vector2 offset = new Vector2();
    public float density;
    public float friction;
    public float restitution;
    public boolean isSolid = true;
    
    // Visual data
    public final Color color = new Color();
    public String animationName;
    public String animationSelection;
    public final Vector2 animationRoot = new Vector2(0.0f);
    public final Vector2 animationScale = new Vector2(1.0f);
    public String textureName;
    public final Vector2 texturePoint = new Vector2(Float.NaN);
    public final Vector2 textureSize = new Vector2(Float.NaN);
    
    // Engine data
    public Body engineBody;
    public Fixture[] engineFixtures;
    
    public void validate() throws InvalidPhysicsShapeException
    {
        if (offset == null)       throw new InvalidPhysicsShapeException("invalid shape position: null");
        if (density < 0)          throw new InvalidPhysicsShapeException("invalid shape density: negative");
        if (friction < 0)         throw new InvalidPhysicsShapeException("invalid shape friction: negative");
        if (restitution < 0)      throw new InvalidPhysicsShapeException("invalid shape restitution: negative");

        if (color == null)        throw new InvalidPhysicsShapeException("invalid shape color: null");
        if (color.r > 1)          throw new InvalidPhysicsShapeException("invalid shape color: red > 1");
        if (color.r < 0)          throw new InvalidPhysicsShapeException("invalid shape color: red < 0");
        if (color.g > 1)          throw new InvalidPhysicsShapeException("invalid shape color: green > 1");
        if (color.g < 0)          throw new InvalidPhysicsShapeException("invalid shape color: green < 0");
        if (color.b > 1)          throw new InvalidPhysicsShapeException("invalid shape color: blue > 1");
        if (color.b < 0)          throw new InvalidPhysicsShapeException("invalid shape color: blue < 0");
        if (color.a > 1)          throw new InvalidPhysicsShapeException("invalid shape color: alpha > 1");
        if (color.a < 0)          throw new InvalidPhysicsShapeException("invalid shape color: alpha < 0");
        if (animationRoot == null) throw new InvalidPhysicsShapeException("invalid shape animation root: null");
        if (animationScale == null)  throw new InvalidPhysicsShapeException("invalid shape animation scale: null");
        if (texturePoint == null) throw new InvalidPhysicsShapeException("invalid shape texture point: null");
        if (textureSize == null)  throw new InvalidPhysicsShapeException("invalid shape texture size: null");
    }
    
    public void create(Body engineBody)
    {
        this.engineBody = engineBody;
    }
    
    public void destroy(Body engineBody)
    {
        for (Fixture fixture : engineFixtures)
            engineBody.destroyFixture(fixture);
    }
    
    public void update()
    {
        for (Fixture fixture : engineFixtures)
        {
            fixture.setDensity(density);
            fixture.setFriction(friction);
            fixture.setRestitution(restitution);
            fixture.setSensor(!isSolid);
            fixture.m_body.resetMassData();
        }
    }
    
    public List<PhysicsShapeSnapshot> getSnapshots()
    {
        List<PhysicsShapeSnapshot> snapshots = new ArrayList<PhysicsShapeSnapshot>();
        for (int i = 0; i < engineFixtures.length; i++)
            snapshots.add(buildSnapshot(i));
        return snapshots;
    }
    
    protected PhysicsShapeSnapshot buildSnapshot(int shapeIndex)
    {
        PhysicsShapeSnapshot snapshot = new PhysicsShapeSnapshot();
        snapshot.attachments = new PhysicsShapeAttachments();
        snapshot.attachments.color.set(color);
        snapshot.attachments.animationName = animationName;
        snapshot.attachments.animationSelection = animationSelection;
        snapshot.attachments.animationRoot.set(animationRoot);
        snapshot.attachments.animationScale.set(animationScale);
        snapshot.attachments.textureName = textureName;
        snapshot.attachments.texturePoint.set(texturePoint);
        snapshot.attachments.textureSize.set(textureSize);
        return snapshot;
    }
    
    public boolean isRelevantToClient()
    {
        return isSolid;
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
}
