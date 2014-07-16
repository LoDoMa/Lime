package net.lodoma.lime.physics;

public class ServersidePhysicsPool extends PhysicsPool
{
    private static class ServersidePoolListener implements PoolListener
    {
        @Override
        public void onNewBody(PhysicsPool pool, PhysicsBody body)
        {
            
        }
        
        @Override
        public void onNewJoint(PhysicsPool pool, PhysicsJoint joint)
        {
            
        }
    }
    
    public ServersidePhysicsPool()
    {
        super();
        addListener(new ServersidePoolListener());
    }
    
    @Override
    public void fetch()
    {
        super.fetch();
        
    }
}
