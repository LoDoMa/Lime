package net.joritan.jlime.util;

public class Timer
{
    private long lastTime;
    private float scale;

    public Timer()
    {
        lastTime = System.nanoTime();
        scale = 1.0f;
    }

    public void reset()
    {
        lastTime = System.nanoTime();
    }

    public void setScale(float scale)
    {
        this.scale = scale;
    }

    public float update()
    {
        long curTime = System.nanoTime();
        long nanoDelta = curTime - lastTime;
        lastTime = curTime;
        float secDelta = nanoDelta / 1000000000f;
        return secDelta * scale;
    }
}
