package net.joritan.esc;

import java.io.IOException;
import java.io.InputStream;

public class ESCInputStream
{
    private final byte[] buff2 = new byte[2];
    private final byte[] buff4 = new byte[4];
    private final byte[] buff8 = new byte[8];

    private InputStream stream;

    public ESCInputStream(InputStream stream)
    {
        this.stream = stream;
    }

    public byte readByte()
    {
        try
        {
        return (byte) stream.read(); // ???
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public short readShort()
    {
        try
        {
            stream.read(buff2);
            return (short) (buff2[0] << 8 | buff2[1]); // ???
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public int readInt()
    {
        try
        {
            stream.read(buff4);
            return buff4[0] << 24 | buff4[1] << 16 | buff4[2] << 8 | buff4[3];
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public int readLong()
    {
        try
        {
            stream.read(buff8);
            return buff8[0] << 56 | buff8[1] << 48 | buff8[2] << 40 | buff8[3] << 32 |
                   buff8[4] << 24 | buff8[5] << 16 | buff8[6] << 8 | buff8[7];
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public float readFloat()
    {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble()
    {
        return Double.longBitsToDouble(readLong());
    }

    public String readString()
    {
        try
        {
            String string = "";
            int nextByte;
            while ((nextByte = stream.read()) != 0)
                string += (char) nextByte;
            return string;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void close()
    {
        try
        {
            stream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
