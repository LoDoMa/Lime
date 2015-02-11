package net.kalinovcic.libxbf.xbt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
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

    public static final int VERSION_100 = 0;
    public static final int VERSION_103 = 1;
    public static final int VERSION_LATEST = VERSION_103;
    
    static final String[] VERSION_NAMEMAP = new String[]
            {
            "100",
            "103"
            };
    
    public static XBTNamedContainer read(File file) throws IOException
    {
        InputStream fileStream = new BufferedInputStream(new FileInputStream(file));
        DataInputStream dataDirect = new DataInputStream(fileStream);
        
        XBTNamedContainer content;
        
        try
        {
            int signature = dataDirect.readInt();
            int version = dataDirect.readUnsignedByte();
            int algorithmID = dataDirect.readUnsignedByte();
            
            if (signature != XBT_HEADER_SIGNATURE)
                throw new InvalidHeaderException("Invalid header signature of XBT file");
            if (version > VERSION_LATEST)
                throw new UnsupportedVersionException();
            if (algorithmID >= CompressionAlgorithm.values().length)
                throw new UnsupportedAlgorithmException("unsupported compression algorithm ID " + algorithmID + " for XBT version " + VERSION_NAMEMAP[version]);
            
            int compressedSize = -1;
            int decompressedSize = -1;
            
            if (version >= VERSION_103)
            {
                /* Read additional header data in version 103 */
                
                compressedSize = dataDirect.readInt(); 
                decompressedSize = dataDirect.readInt();

                if (compressedSize < 0)
                    throw new InvalidHeaderException("compressed size too large: max 2^31 - 1");
                if (decompressedSize < 0)
                    throw new InvalidHeaderException("decompressed size too large: max 2^31 - 1"); 
            }
            else
            {
                /* Compression algorithms after GZIP weren't supported before version 103 */
                
                if (algorithmID > CompressionAlgorithm.GZIP.ID)
                    throw new UnsupportedAlgorithmException("unsupported compression algorithm ID " + algorithmID + " for XBT version " + VERSION_NAMEMAP[version]);
            }
            
            InputStream decompressionStream = null;
            
            try
            {
                if (version >= VERSION_103)
                {
                    /* In version 103 and greater, the file is decompressed all at once.
                       We know the size of the compressed container, so we can just read that many bytes after the header.
                       We also know the decompressed size, so decompression algorithms can work faster. */
                    
                    byte[] compressedData = new byte[compressedSize];
                    if (dataDirect.read(compressedData, 0, compressedSize) < 0)
                        throw new EOFException();
                    
                    byte[] decompressedData = CompressionAlgorithm.values()[algorithmID].decompress(compressedData, decompressedSize);
                    decompressionStream = new ByteArrayInputStream(decompressedData);
                }
                else
                {
                    /* In versions before 103, the file decompression is streamed,
                       because we don't know neither the compressed or the decompressed size.
                       Note that this doesn't work for some new algorithms, which is why
                       version 100 is now deprecated. */
                    
                    decompressionStream = CompressionAlgorithm.values()[algorithmID].decompressionStream(fileStream);
                }
                
                /* Create and load the NamedContainer after the header. */
                content = new XBTNamedContainer();
                content.read(new DataInputStream(decompressionStream), version);
            }
            finally
            {
                if (decompressionStream != null)
                    decompressionStream.close();
            }
            
        }
        finally
        {
            dataDirect.close();
            fileStream.close();
        }
        
        return content;
    }
    
    public static void write(File file, XBTNamedContainer content, CompressionAlgorithm algorithm, int version) throws IOException
    {
        if (version < 0 || version > VERSION_LATEST)
            throw new UnsupportedVersionException();
        if (version < VERSION_103)
            if (algorithm.ID > CompressionAlgorithm.GZIP.ID)
                throw new UnsupportedAlgorithmException("unsupported compression algorithm " + algorithm.name() + " for XBT version " + VERSION_NAMEMAP[version]);
        
        OutputStream fileStream = new BufferedOutputStream(new FileOutputStream(file));
        DataOutputStream dataDirect = new DataOutputStream(fileStream);
        
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dataUncompressed = new DataOutputStream(baos);
            content.write(dataUncompressed, version);
            
            byte[] uncompressedData = baos.toByteArray();
            byte[] compressedData = algorithm.compress(uncompressedData);
            
            dataDirect.writeInt(XBT_HEADER_SIGNATURE);
            dataDirect.writeByte(version);
            dataDirect.writeByte(algorithm.ID);
            
            if (version >= VERSION_103)
            {
                /* Write additional header data in version 103 */
                
                dataDirect.writeInt(compressedData.length);
                dataDirect.writeInt(uncompressedData.length);
            }
            
            dataDirect.write(compressedData, 0, compressedData.length);
        }
        finally
        {
            dataDirect.close();
            fileStream.close();
        }
    }
}
