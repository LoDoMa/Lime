package net.kalinovcic.libxbf.xbt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.kalinovcic.libxbf.commons.CompressionAlgorithm;
import net.kalinovcic.libxbf.commons.InvalidHeaderException;
import net.kalinovcic.libxbf.commons.UnsupportedAlgorithmException;
import net.kalinovcic.libxbf.commons.UnsupportedVersionException;

public class XBT
{
    private static final int XBT_HEADER_SIGNATURE = 0x5842541B;

    private static final int VERSION_100 = 0;
    private static final int VERSION_LATEST = VERSION_100;
    
    public static XBTNamedContainer read(File file) throws IOException
    {
        InputStream fileStream = new BufferedInputStream(new FileInputStream(file));
        DataInputStream dataDirect = new DataInputStream(fileStream);
        
        int algorithmID;
        
        if (dataDirect.readInt() != XBT_HEADER_SIGNATURE)
        {
            dataDirect.close();
            fileStream.close();
            throw new InvalidHeaderException();
        }
        if (dataDirect.readUnsignedByte() > VERSION_LATEST)
        {
            dataDirect.close();
            fileStream.close();
            throw new UnsupportedVersionException();
        }
        algorithmID = dataDirect.readUnsignedByte();
        if (algorithmID >= CompressionAlgorithm.values().length)
        {
            dataDirect.close();
            fileStream.close();
            throw new UnsupportedAlgorithmException("unknown compression algorithm ID " + algorithmID);
        }
        
        CompressionAlgorithm algorithm = CompressionAlgorithm.values()[algorithmID];
        
        InputStream compressedStream = algorithm.newIS(fileStream);
        DataInputStream dataCompressed = new DataInputStream(compressedStream);
        
        XBTNamedContainer content = new XBTNamedContainer();
        content.read(dataCompressed);
        
        dataCompressed.close();
        compressedStream.close();
        
        dataDirect.close();
        fileStream.close();
        
        return content;
    }
    
    public static void write(File file, XBTNamedContainer content, CompressionAlgorithm algorithm) throws IOException
    {
        OutputStream fileStream = new BufferedOutputStream(new FileOutputStream(file));
        DataOutputStream dataDirect = new DataOutputStream(fileStream);
        
        dataDirect.writeInt(XBT_HEADER_SIGNATURE);
        dataDirect.writeByte(VERSION_100);
        dataDirect.writeByte(algorithm.ID);

        OutputStream compressedStream = algorithm.newOS(fileStream);
        DataOutputStream dataCompressed = new DataOutputStream(compressedStream);
        
        content.write(dataCompressed);
        
        dataCompressed.close();
        compressedStream.close();
        
        dataDirect.close();
        fileStream.close();
    }
}
