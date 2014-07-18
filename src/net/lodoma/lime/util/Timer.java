package net.lodoma.lime.util;

public class Timer
{
    private long curTime;
    private long lastTime;
    
    private double delta;
    
    public Timer()
    {
        this.lastTime = System.nanoTime();
        this.curTime = System.nanoTime();
    }
    
    public double getDelta()
    {
        return this.delta;
    }
    
    public void update()
    {
        this.curTime = System.nanoTime();
        
        this.delta = this.curTime - this.lastTime;
        this.delta /= 1000000000f;
        
        this.lastTime = this.curTime;
    }
}
