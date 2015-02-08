package net.kalinovcic.libxbf.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public enum CompressionAlgorithm
{
    NONE(new CompressionISFactory()
    {
        @Override
        public InputStream newIS(InputStream source) throws IOException
        {
            return source;
        }
    }, new CompressionOSFactory()
    {
        @Override
        public OutputStream newOS(OutputStream destination) throws IOException
        {
            return destination;
        }
    }),
    
    GZIP(new CompressionISFactory()
    {
        @Override
        public InputStream newIS(InputStream source) throws IOException
        {
            return new GZIPInputStream(source);
        }
    }, new CompressionOSFactory()
    {
        @Override
        public OutputStream newOS(OutputStream destination) throws IOException
        {
            return new GZIPOutputStream(destination);
        }
    });
    
    public final int ID;
    
    private final CompressionISFactory isFactory;
    private final CompressionOSFactory osFactory;
    
    CompressionAlgorithm(CompressionISFactory isFactory, CompressionOSFactory osFactory)
    {
        ID = ordinal();
        this.isFactory = isFactory;
        this.osFactory = osFactory;
    }
    
    public OutputStream newOS(OutputStream destination) throws IOException
    {
        return osFactory.newOS(destination);
    }
    
    public InputStream newIS(InputStream destination) throws IOException
    {
        return isFactory.newIS(destination);
    }
    
    private static interface CompressionISFactory
    {
        public InputStream newIS(InputStream source) throws IOException;
    }
    
    private static interface CompressionOSFactory
    {
        public OutputStream newOS(OutputStream destination) throws IOException;
    }
}
