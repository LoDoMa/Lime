package net.lodoma.lime.util;

public class ThreadHelper
{
    public static void waitWhileAlive(Thread thread) throws InterruptedException
    {
        while(thread.isAlive())
            Thread.sleep(0, 100000);
    }
    
    public static void interruptAndWait(Thread thread) throws InterruptedException
    {
        thread.interrupt();
        waitWhileAlive(thread);
    }
    
    @SuppressWarnings("deprecation")
    public static void forceStop(Thread thread) throws InterruptedException
    {
        thread.stop();
        waitWhileAlive(thread);
    }
}
