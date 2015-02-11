package net.kalinovcic.libxbf.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

public enum CompressionAlgorithm
{
    NONE
    {
        @Override
        public byte[] compress(byte[] uncompressedData) throws IOException
        {
            return uncompressedData;
        }
        
        @Override
        public byte[] decompress(byte[] compressedData, int decompressedSize) throws IOException
        {
            return compressedData;
        }
        
        @Override
        public InputStream decompressionStream(InputStream source) throws IOException
        {
            return source;
        }
    },
    
    GZIP
    {
        @Override
        public byte[] compress(byte[] uncompressedData) throws IOException
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(baos);
            gzip.write(uncompressedData, 0, uncompressedData.length);
            gzip.close();
            return baos.toByteArray();
        }
        
        @Override
        public byte[] decompress(byte[] compressedData, int decompressedSize) throws IOException
        {
            ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
            GZIPInputStream gzip = new GZIPInputStream(bais);
            byte[] decompressed = new byte[decompressedSize];
            gzip.read(decompressed, 0, decompressed.length);
            gzip.close();
            return decompressed;
        }
        
        @Override
        public InputStream decompressionStream(InputStream source) throws IOException
        {
            return new GZIPInputStream(source);
        }
    },
    
    LZ4
    {
        @Override
        public byte[] compress(byte[] uncompressedData) throws IOException
        {
            LZ4Compressor compressor = factory.fastCompressor();
            byte[] compressedMax = new byte[compressor.maxCompressedLength(uncompressedData.length)];
            int compressedSize = compressor.compress(uncompressedData, 0, uncompressedData.length, compressedMax, 0, compressedMax.length);
            byte[] compressed = new byte[compressedSize];
            System.arraycopy(compressedMax, 0, compressed, 0, compressed.length);
            return compressed;
        }
        
        @Override
        public byte[] decompress(byte[] compressedData, int decompressedSize) throws IOException
        {
            LZ4FastDecompressor decompressor = factory.fastDecompressor();
            byte[] decompressed = new byte[decompressedSize];
            decompressor.decompress(compressedData, 0, decompressed, 0, decompressedSize);
            return decompressed;
        }
        
        @Override
        public InputStream decompressionStream(InputStream source) throws IOException
        {
            throw new UnsupportedOperationException("LZ4 doesn't support streamed decompression");
        }
    };
    
    public final int ID;
    
    CompressionAlgorithm()
    {
        ID = ordinal();
    }

    public abstract byte[] compress(byte[] uncompressedData) throws IOException;
    public abstract byte[] decompress(byte[] compressedData, int decompressedSize) throws IOException;
    public abstract InputStream decompressionStream(InputStream source) throws IOException;
    
    private static LZ4Factory factory = LZ4Factory.fastestInstance();
}
