package net.lodoma.lime.util;

public class ThreadHelper
{
    public void interruptAndWait(Thread thread) throws InterruptedException
    {
        thread.interrupt();
        while(thread.isAlive())
            Thread.sleep(0, 100000);
    }
}
