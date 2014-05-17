package net.joritan.esc;

import java.io.IOException;
import java.io.OutputStream;

public class ESCOutputStream
{
    private OutputStream stream;

    public ESCOutputStream(OutputStream stream)
    {
        this.stream = stream;
    }

    public void writeByte(byte value)
    {
        try
        {
            stream.write(value);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void writeShort(short value)
    {
        try
        {
            stream.write((value >> 8) & 0xFF);
            stream.write(value & 0xFF);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void writeInt(int value)
    {
        try
        {
            stream.write((value >> 24) & 0xFF);
            stream.write((value >> 16) & 0xFF);
            stream.write((value >> 8) & 0xFF);
            stream.write(value & 0xFF);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void writeLong(long value)
    {
        try
        {
            stream.write((int) ((value >> 56) & 0xFF));
            stream.write((int) ((value >> 48) & 0xFF));
            stream.write((int) ((value >> 40) & 0xFF));
            stream.write((int) ((value >> 32) & 0xFF));
            stream.write((int) ((value >> 24) & 0xFF));
            stream.write((int) ((value >> 16) & 0xFF));
            stream.write((int) ((value >> 8) & 0xFF));
            stream.write((int) (value & 0xFF));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void writeFloat(float value)
    {
        writeInt(Float.floatToRawIntBits(value));
    }

    public void writeDouble(double value)
    {
        writeLong(Double.doubleToRawLongBits(value));
    }

    public void writeString(String value)
    {
        byte[] bytes = value.getBytes();
        for(byte i : bytes)
            writeByte(i);
        writeByte((byte) 0);
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
