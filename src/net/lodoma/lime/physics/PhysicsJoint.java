package net.lodoma.lime.physics;

import java.nio.ByteBuffer;

import net.lodoma.lime.util.Vector2;

import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class PhysicsJoint
{
    private static final int INDEX_ID = 0;
    private static final int SIZE_ID = 8;
    private static final int INDEX_TYPE = INDEX_ID + SIZE_ID;
    private static final int SIZE_TYPE = 1;
    private static final int INDEX_BODYIDA = INDEX_TYPE + SIZE_TYPE;
    private static final int SIZE_BODYIDA = 8;
    private static final int INDEX_BODYIDB = INDEX_BODYIDA + SIZE_BODYIDA;
    private static final int SIZE_BODYIDB = 8;
    private static final int INDEX_COLLISION = INDEX_BODYIDB + SIZE_BODYIDB;
    private static final int SIZE_COLLISION = 1;
    private static final int INDEX_ANCHORXA = INDEX_COLLISION + SIZE_COLLISION;
    private static final int SIZE_ANCHORXA = 4;
    private static final int INDEX_ANCHORYA = INDEX_ANCHORXA + SIZE_ANCHORXA;
    private static final int SIZE_ANCHORYA = 4;
    private static final int INDEX_ANCHORXB = INDEX_ANCHORYA + SIZE_ANCHORYA;
    private static final int SIZE_ANCHORXB = 4;
    private static final int INDEX_ANCHORYB = INDEX_ANCHORXB + SIZE_ANCHORXB;
    private static final int SIZE_ANCHORYB = 4;
    private static final int INDEX_MOTOR = INDEX_ANCHORYB + SIZE_ANCHORYB;
    private static final int SIZE_MOTOR = 1;
    private static final int INDEX_MOTOR_SPEED = INDEX_MOTOR + SIZE_MOTOR;
    private static final int SIZE_MOTOR_SPEED = 4;
    private static final int INDEX_MAX_MOTOR_TORQUE = INDEX_MOTOR_SPEED + SIZE_MOTOR_SPEED;
    private static final int SIZE_MAX_MOTOR_TORQUE = 4;
    private static final int INDEX_LIMIT = INDEX_MAX_MOTOR_TORQUE + SIZE_MAX_MOTOR_TORQUE;
    private static final int SIZE_LIMIT = 1;
    private static final int INDEX_LIMIT_LOWER = INDEX_LIMIT + SIZE_LIMIT;
    private static final int SIZE_LIMIT_LOWER = 4;
    private static final int INDEX_LIMIT_UPPER = INDEX_LIMIT_LOWER + SIZE_LIMIT_LOWER;
    private static final int SIZE_LIMIT_UPPER = 4;
    private static final int INDEX_ANGLE = INDEX_LIMIT_UPPER + SIZE_LIMIT_UPPER;
    private static final int SIZE_ANGLE = 4;
    private static final int TOTAL_SIZE = INDEX_ANGLE + SIZE_ANGLE;

    private static long counterID = 0;
    private long ID;
    
    private PhysicsJointType type;
    private JointDef jd;
    private Joint joint;
    
    private ByteBuffer bytes;
    
    public PhysicsJoint()
    {
        bytes = ByteBuffer.allocate(TOTAL_SIZE);
        bytes.put(INDEX_TYPE, (byte) type.ordinal());
    }
    
    public PhysicsJoint(PhysicsJointType type)
    {
        bytes = ByteBuffer.allocate(TOTAL_SIZE);
        bytes.put(INDEX_TYPE, (byte) type.ordinal());
        
        this.type = type;
        jd = type.jointDef();
    }
    
    public long getID()
    {
        return ID;
    }
    
    public void generateID()
    {
        bytes.putLong(INDEX_ID, counterID++);
    }
    
    public void setBodyA(long bodyA_ID)
    {
        bytes.putLong(INDEX_BODYIDA, bodyA_ID);
    }
    
    public void setBodyB(long bodyB_ID)
    {
        bytes.putLong(INDEX_BODYIDB, bodyB_ID);
    }
    
    public void setCollisionEnabled(boolean collideConnected)
    {
        bytes.put(INDEX_COLLISION, collideConnected ? (byte) 1 : (byte) 0);
    }
    
    public void setAnchorA(Vector2 anchorA)
    {
        bytes.putFloat(INDEX_ANCHORXA, anchorA.x);
        bytes.putFloat(INDEX_ANCHORYA, anchorA.y);
    }
    
    public void setAnchorB(Vector2 anchorB)
    {
        bytes.putFloat(INDEX_ANCHORXB, anchorB.x);
        bytes.putFloat(INDEX_ANCHORYB, anchorB.y);
    }
    
    public void setMotorEnabled(boolean motor)
    {
        bytes.put(INDEX_MOTOR, motor ? (byte) 1 : (byte) 0);
    }
    
    public void setMotorSpeed(float motorSpeed)
    {
        bytes.putFloat(INDEX_MOTOR_SPEED, motorSpeed);
    }
    
    public void setMaxMotorTorque(float maxMotorTorque)
    {
        bytes.putFloat(INDEX_MAX_MOTOR_TORQUE, maxMotorTorque);
    }
    
    public void setLimitEnabled(boolean limit)
    {
        bytes.put(INDEX_LIMIT, limit ? (byte) 1 : (byte) 0);
    }
    
    public void setLimits(float lower, float upper)
    {
        bytes.putFloat(INDEX_LIMIT_LOWER, lower);
        bytes.putFloat(INDEX_LIMIT_UPPER, upper);
    }
    
    public void setAngle(float angle)
    {
        bytes.putFloat(INDEX_ANGLE, angle);
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
    
    public void reload(PhysicsPool pool)
    {
        long id = bytes.getLong(INDEX_ID);
        PhysicsJointType type = PhysicsJointType.values()[bytes.get(INDEX_TYPE)];
        long bodyA_ID = bytes.getLong(INDEX_BODYIDA);
        long bodyB_ID = bytes.getLong(INDEX_BODYIDB);
        PhysicsBody bodyA = pool.getBody(bodyA_ID);
        PhysicsBody bodyB = pool.getBody(bodyB_ID);
        boolean collideConnected = bytes.get(INDEX_COLLISION) == 1;
        float anchorAX = bytes.getLong(INDEX_ANCHORXA);
        float anchorAY = bytes.getLong(INDEX_ANCHORYA);
        float anchorBX = bytes.getLong(INDEX_ANCHORXB);
        float anchorBY = bytes.getLong(INDEX_ANCHORYB);
        boolean motor = bytes.get(INDEX_MOTOR) == 1;
        float motorSpeed = bytes.getFloat(INDEX_MOTOR_SPEED);
        float maxMotorTorque = bytes.getFloat(INDEX_MAX_MOTOR_TORQUE);
        boolean limit = bytes.get(INDEX_LIMIT) == 1;
        float limitLower = bytes.getFloat(INDEX_LIMIT_LOWER);
        float limitUpper = bytes.getFloat(INDEX_LIMIT_UPPER);
        float angle = bytes.getFloat(INDEX_ANGLE);
        
        jd = type.jointDef();
        
        ID = id;
        jd.bodyA = bodyA.getEngineBody();
        jd.bodyB = bodyB.getEngineBody();
        jd.collideConnected = collideConnected;
        jd.type = type.getEngineType();
        jd.userData = ID;
        
        switch(type)
        {
        case REVOLUTE:
            ((RevoluteJointDef) jd).localAnchorA.set(anchorAX, anchorAY);
            ((RevoluteJointDef) jd).localAnchorB.set(anchorBX, anchorBY);
            if(motor)
            {
                ((RevoluteJointDef) jd).enableMotor = motor;
                ((RevoluteJointDef) jd).motorSpeed = motorSpeed;
                ((RevoluteJointDef) jd).maxMotorTorque = maxMotorTorque;
            }
            if(limit)
            {
                ((RevoluteJointDef) jd).enableLimit = true;
                ((RevoluteJointDef) jd).lowerAngle = limitLower;
                ((RevoluteJointDef) jd).upperAngle = limitUpper;
            }
            ((RevoluteJointDef) jd).referenceAngle = angle;
        default: break;
        }
    }
    
    public Joint getEngineJoint()
    {
        return joint;
    }
    
    public void create(PhysicsWorld world)
    {
        World engineWorld = world.getEngineWorld();
        joint = engineWorld.createJoint(jd);
    }
    
    public void destroy(PhysicsWorld world)
    {
        World engineWorld = world.getEngineWorld();
        engineWorld.destroyJoint(joint);
    }
}
