package net.lodoma.lime.util;

import java.io.IOException;
import java.io.OutputStream;

public class FastPOS extends OutputStream
{
    FastPIS sink;
    
    public FastPOS(FastPIS sink)
    {
        this.sink = sink;
    }
    
    @Override
    public void write(int b) throws IOException
    {
        write(new byte[] { (byte) b }, 0, 1);
    }
    
    @Override
    public void write(byte[] b) throws IOException
    {
        write(b, 0, b.length);
    }
    
    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        synchronized(sink.buffer)
        {
            while (sink.writepos == sink.readpos && sink.writelap == (sink.readlap + 1))
            {
                try
                {
                    sink.buffer.wait();
                }
                catch(InterruptedException e)
                {
                    throw new IOException(e.getMessage());
                }
            }
            
            int amount = Math.min(len, (sink.writepos < sink.readpos ? sink.readpos : sink.buffer.length) - sink.writepos);
            System.arraycopy(b, off, sink.buffer, sink.writepos, amount);
            sink.writepos += amount;
            
            sink.buffer.notifyAll();
            
            if (sink.writepos == sink.buffer.length)
            {
                sink.writepos = 0;
                sink.writelap++;
            }
            
            if (amount < len)
                write(b, off + amount, len - amount);
        }
    }
}
