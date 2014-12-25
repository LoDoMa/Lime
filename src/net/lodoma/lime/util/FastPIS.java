package net.lodoma.lime.util;

import java.io.IOException;
import java.io.InputStream;

public class FastPIS extends InputStream
{
    byte[] buffer = new byte[1024];
    int readpos = 0;
    int readlap = 0;
    int writepos = 0;
    int writelap = 0;
    
    @Override
    public int available() throws IOException
    {
        return writepos > readpos ? writepos - readpos :
              (writepos < readpos ? buffer.length - readpos + 1 + writepos :
              (writelap == (readlap + 1) ? buffer.length : 0));
    }
    
    @Override
    public int read() throws IOException
    {
        byte[] buff = new byte[1];
        read(buff, 0, 1);
        return buff[0] & 0xFF;
    }
    
    @Override
    public int read(byte[] b) throws IOException
    {
        return read(b, 0, b.length);
    }
    
    @Override
    public int read(byte[] b, int off, int len) throws IOException
    {
        synchronized(buffer)
        {
            while (readpos == writepos && readlap == writelap)
            {
                try
                {
                    buffer.wait();
                }
                catch(InterruptedException e)
                {
                    throw new IOException(e.getMessage());
                }
            }
            
            int amount = Math.min(len, (writepos > readpos ? writepos : buffer.length) - readpos);
            System.arraycopy(buffer, readpos, b, off, amount);
            readpos += amount;
            
            buffer.notifyAll();
            
            if (readpos == buffer.length)
            {
                readpos = 0;
                readlap++;
            }
            
            if (amount < len)
            {
                int next = read(b, off + amount, len - amount);
                return amount + next;
            }
            
            return amount;
        }
    }
}
