package net.kalinovcic.libxbf.commons;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class StringITA2
{
    private static final byte CHARDESC_SHIFTON  = 0x20;
    private static final byte CHARDESC_SHIFTOFF = 0x40;
    private static final byte CHARDESC_ITAMASK  = 0x1F;
    
    private static final byte[] ITA2_CHARS = new byte[128];
    private static final byte[] CHARS_ITA2 = new byte[128];
    
    static
    {
        ITA2_CHARS[0]    = 0x00 | CHARDESC_SHIFTOFF | CHARDESC_SHIFTON;
        ITA2_CHARS[10]   = 0x02 | CHARDESC_SHIFTOFF | CHARDESC_SHIFTON;
        ITA2_CHARS[32]   = 0x04 | CHARDESC_SHIFTOFF | CHARDESC_SHIFTON;
        ITA2_CHARS[13]   = 0x08 | CHARDESC_SHIFTOFF | CHARDESC_SHIFTON;
        ITA2_CHARS[14]   = 0x1B | CHARDESC_SHIFTOFF | CHARDESC_SHIFTON;
        ITA2_CHARS[15]   = 0x1F | CHARDESC_SHIFTOFF | CHARDESC_SHIFTON;

        CHARS_ITA2[0x00 | CHARDESC_SHIFTOFF] = 0;
        CHARS_ITA2[0x00 | CHARDESC_SHIFTON]  = 0;
        CHARS_ITA2[0x02 | CHARDESC_SHIFTOFF] = 10;
        CHARS_ITA2[0x02 | CHARDESC_SHIFTON]  = 10;
        CHARS_ITA2[0x04 | CHARDESC_SHIFTOFF] = 32;
        CHARS_ITA2[0x04 | CHARDESC_SHIFTON]  = 32;
        CHARS_ITA2[0x08 | CHARDESC_SHIFTOFF] = 13;
        CHARS_ITA2[0x08 | CHARDESC_SHIFTON]  = 13;
        CHARS_ITA2[0x1B | CHARDESC_SHIFTOFF] = 14;
        CHARS_ITA2[0x1B | CHARDESC_SHIFTON]  = 14;
        CHARS_ITA2[0x1F | CHARDESC_SHIFTOFF] = 15;
        CHARS_ITA2[0x1F | CHARDESC_SHIFTON]  = 15;
        
        ITA2_CHARS['E']  = 0x01 | CHARDESC_SHIFTOFF;
        ITA2_CHARS['A']  = 0x03 | CHARDESC_SHIFTOFF;
        ITA2_CHARS['S']  = 0x05 | CHARDESC_SHIFTOFF;
        ITA2_CHARS['I']  = 0x06 | CHARDESC_SHIFTOFF;
        ITA2_CHARS['U']  = 0x07 | CHARDESC_SHIFTOFF;
        ITA2_CHARS['D']  = 0x09 | CHARDESC_SHIFTOFF;
        ITA2_CHARS['R']  = 0x0A | CHARDESC_SHIFTOFF;
        ITA2_CHARS['J']  = 0x0B | CHARDESC_SHIFTOFF;
        ITA2_CHARS['N']  = 0x0C | CHARDESC_SHIFTOFF;
        ITA2_CHARS['F']  = 0x0D | CHARDESC_SHIFTOFF;
        ITA2_CHARS['C']  = 0x0E | CHARDESC_SHIFTOFF;
        ITA2_CHARS['K']  = 0x0F | CHARDESC_SHIFTOFF;
        ITA2_CHARS['T']  = 0x10 | CHARDESC_SHIFTOFF;
        ITA2_CHARS['Z']  = 0x11 | CHARDESC_SHIFTOFF;
        ITA2_CHARS['L']  = 0x12 | CHARDESC_SHIFTOFF;
        ITA2_CHARS['W']  = 0x13 | CHARDESC_SHIFTOFF;
        ITA2_CHARS['H']  = 0x14 | CHARDESC_SHIFTOFF;
        ITA2_CHARS['Y']  = 0x15 | CHARDESC_SHIFTOFF;
        ITA2_CHARS['P']  = 0x16 | CHARDESC_SHIFTOFF;
        ITA2_CHARS['Q']  = 0x17 | CHARDESC_SHIFTOFF;
        ITA2_CHARS['O']  = 0x18 | CHARDESC_SHIFTOFF;
        ITA2_CHARS['B']  = 0x19 | CHARDESC_SHIFTOFF;
        ITA2_CHARS['G']  = 0x1A | CHARDESC_SHIFTOFF;
        ITA2_CHARS['M']  = 0x1C | CHARDESC_SHIFTOFF;
        ITA2_CHARS['X']  = 0x1D | CHARDESC_SHIFTOFF;
        ITA2_CHARS['V']  = 0x1E | CHARDESC_SHIFTOFF;

        CHARS_ITA2[0x01 | CHARDESC_SHIFTOFF] = 'E';
        CHARS_ITA2[0x03 | CHARDESC_SHIFTOFF] = 'A';
        CHARS_ITA2[0x05 | CHARDESC_SHIFTOFF] = 'S';
        CHARS_ITA2[0x06 | CHARDESC_SHIFTOFF] = 'I';
        CHARS_ITA2[0x07 | CHARDESC_SHIFTOFF] = 'U';
        CHARS_ITA2[0x09 | CHARDESC_SHIFTOFF] = 'D';
        CHARS_ITA2[0x0A | CHARDESC_SHIFTOFF] = 'R';
        CHARS_ITA2[0x0B | CHARDESC_SHIFTOFF] = 'J';
        CHARS_ITA2[0x0C | CHARDESC_SHIFTOFF] = 'N';
        CHARS_ITA2[0x0D | CHARDESC_SHIFTOFF] = 'F';
        CHARS_ITA2[0x0E | CHARDESC_SHIFTOFF] = 'C';
        CHARS_ITA2[0x0F | CHARDESC_SHIFTOFF] = 'K';
        CHARS_ITA2[0x10 | CHARDESC_SHIFTOFF] = 'T';
        CHARS_ITA2[0x11 | CHARDESC_SHIFTOFF] = 'Z';
        CHARS_ITA2[0x12 | CHARDESC_SHIFTOFF] = 'L';
        CHARS_ITA2[0x13 | CHARDESC_SHIFTOFF] = 'W';
        CHARS_ITA2[0x14 | CHARDESC_SHIFTOFF] = 'H';
        CHARS_ITA2[0x15 | CHARDESC_SHIFTOFF] = 'Y';
        CHARS_ITA2[0x16 | CHARDESC_SHIFTOFF] = 'P';
        CHARS_ITA2[0x17 | CHARDESC_SHIFTOFF] = 'Q';
        CHARS_ITA2[0x18 | CHARDESC_SHIFTOFF] = 'O';
        CHARS_ITA2[0x19 | CHARDESC_SHIFTOFF] = 'B';
        CHARS_ITA2[0x1A | CHARDESC_SHIFTOFF] = 'G';
        CHARS_ITA2[0x1C | CHARDESC_SHIFTOFF] = 'M';
        CHARS_ITA2[0x1D | CHARDESC_SHIFTOFF] = 'X';
        CHARS_ITA2[0x1E | CHARDESC_SHIFTOFF] = 'V';
        
        ITA2_CHARS['3']  = 0x01 | CHARDESC_SHIFTON;
        ITA2_CHARS['-']  = 0x03 | CHARDESC_SHIFTON;
        ITA2_CHARS['\''] = 0x05 | CHARDESC_SHIFTON;
        ITA2_CHARS['8']  = 0x06 | CHARDESC_SHIFTON;
        ITA2_CHARS['7']  = 0x07 | CHARDESC_SHIFTON;
        ITA2_CHARS[5]    = 0x09 | CHARDESC_SHIFTON;
        ITA2_CHARS['4']  = 0x0A | CHARDESC_SHIFTON;
        ITA2_CHARS[7]    = 0x0B | CHARDESC_SHIFTON;
        ITA2_CHARS[',']  = 0x0C | CHARDESC_SHIFTON;
        ITA2_CHARS['!']  = 0x0D | CHARDESC_SHIFTON;
        ITA2_CHARS[':']  = 0x0E | CHARDESC_SHIFTON;
        ITA2_CHARS['(']  = 0x0F | CHARDESC_SHIFTON;
        ITA2_CHARS['5']  = 0x10 | CHARDESC_SHIFTON;
        ITA2_CHARS['+']  = 0x11 | CHARDESC_SHIFTON;
        ITA2_CHARS[')']  = 0x12 | CHARDESC_SHIFTON;
        ITA2_CHARS['2']  = 0x13 | CHARDESC_SHIFTON;
        ITA2_CHARS['$']  = 0x14 | CHARDESC_SHIFTON;
        ITA2_CHARS['6']  = 0x15 | CHARDESC_SHIFTON;
        ITA2_CHARS['0']  = 0x16 | CHARDESC_SHIFTON;
        ITA2_CHARS['1']  = 0x17 | CHARDESC_SHIFTON;
        ITA2_CHARS['9']  = 0x18 | CHARDESC_SHIFTON;
        ITA2_CHARS['?']  = 0x19 | CHARDESC_SHIFTON;
        ITA2_CHARS['&']  = 0x1A | CHARDESC_SHIFTON;
        ITA2_CHARS['.']  = 0x1C | CHARDESC_SHIFTON;
        ITA2_CHARS['/']  = 0x1D | CHARDESC_SHIFTON;
        ITA2_CHARS[';']  = 0x1E | CHARDESC_SHIFTON;

        CHARS_ITA2[0x01 | CHARDESC_SHIFTON] = '3';
        CHARS_ITA2[0x03 | CHARDESC_SHIFTON] = '-';
        CHARS_ITA2[0x05 | CHARDESC_SHIFTON] = '\'';
        CHARS_ITA2[0x06 | CHARDESC_SHIFTON] = '8';
        CHARS_ITA2[0x07 | CHARDESC_SHIFTON] = '7';
        CHARS_ITA2[0x09 | CHARDESC_SHIFTON] = 5;
        CHARS_ITA2[0x0A | CHARDESC_SHIFTON] = '4';
        CHARS_ITA2[0x0B | CHARDESC_SHIFTON] = 7;
        CHARS_ITA2[0x0C | CHARDESC_SHIFTON] = ',';
        CHARS_ITA2[0x0D | CHARDESC_SHIFTON] = '!';
        CHARS_ITA2[0x0E | CHARDESC_SHIFTON] = ':';
        CHARS_ITA2[0x0F | CHARDESC_SHIFTON] = '(';
        CHARS_ITA2[0x10 | CHARDESC_SHIFTON] = '5';
        CHARS_ITA2[0x11 | CHARDESC_SHIFTON] = '+';
        CHARS_ITA2[0x12 | CHARDESC_SHIFTON] = ')';
        CHARS_ITA2[0x13 | CHARDESC_SHIFTON] = '2';
        CHARS_ITA2[0x14 | CHARDESC_SHIFTON] = '$';
        CHARS_ITA2[0x15 | CHARDESC_SHIFTON] = '6';
        CHARS_ITA2[0x16 | CHARDESC_SHIFTON] = '0';
        CHARS_ITA2[0x17 | CHARDESC_SHIFTON] = '1';
        CHARS_ITA2[0x18 | CHARDESC_SHIFTON] = '9';
        CHARS_ITA2[0x19 | CHARDESC_SHIFTON] = '?';
        CHARS_ITA2[0x1A | CHARDESC_SHIFTON] = '&';
        CHARS_ITA2[0x1C | CHARDESC_SHIFTON] = '.';
        CHARS_ITA2[0x1D | CHARDESC_SHIFTON] = '/';
        CHARS_ITA2[0x1E | CHARDESC_SHIFTON] = ';';
    }
    
    private byte[] bytes;
    
    /**
     * Creates a new ITA-2 string from a UTF-8 string.
     * 
     * @param str - a UTF-8 string
     * @throws IllegalArgumentException - if the UTF-8 string contains characters not supported by ITA-2
     */
    public StringITA2(String str)
    {
        int length = 0;

        boolean shift = false;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (c < 0 || c > 127 || ITA2_CHARS[c] == 0)
                throw new IllegalArgumentException("string contains characters not supported by ITA-2");
            if (shift && (ITA2_CHARS[c] & CHARDESC_SHIFTON) == 0) { shift = false; length++; }
            else if (!shift && (ITA2_CHARS[c] & CHARDESC_SHIFTOFF) == 0) { shift = true; length++; }
            length++;
        }
        
        bytes = new byte[length + 1];
        
        shift = false;
        for (int i = 0, wi = 0; i < str.length(); i++, wi++)
        {
            char c = str.charAt(i);
            if (c < 0 || c > 127 || ITA2_CHARS[c] == 0)
                throw new IllegalArgumentException("string contains characters not supported by ITA-2");
            if (shift && (ITA2_CHARS[c] & CHARDESC_SHIFTON) == 0)
            {
                bytes[wi++] = (byte) (ITA2_CHARS[15] & CHARDESC_ITAMASK);
                shift = false;
            }
            else if (!shift && (ITA2_CHARS[c] & CHARDESC_SHIFTOFF) == 0)
            {
                bytes[wi++] = (byte) (ITA2_CHARS[14] & CHARDESC_ITAMASK);
                shift = true;
            }
            bytes[wi] = (byte) (ITA2_CHARS[c] & CHARDESC_ITAMASK);
        }
        bytes[length] = (byte) (ITA2_CHARS[0] & CHARDESC_ITAMASK);
    }
    
    /**
     * Creates a new ITA-2 string read from an input stream. Each character is 5 bits,
     * but the total amount of read data is aligned to 8 bits.
     * 
     * @param in - an input stream
     * @throws IOException - thrown by the input stream
     */
    public StringITA2(InputStream in) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        int buffer = 0;
        int fill = 0;
        while (true)
        {
            if (fill < 5)
            {
                buffer = (buffer << 8) | in.read();
                fill += 8;
            }
            int bytez = (buffer >>> (fill - 5)) & 0x1F;
            fill -= 5;
            
            baos.write(bytez);
            if (bytez == 0) break;
        }
        
        bytes = baos.toByteArray();
    }
    
    /**
     * Writes this ITA-2 string to an output stream. Each character is 5 bits,
     * but the total amount of output is aligned to 8 bits.
     * 
     * @param out - an output stream
     * @throws IOException - thrown by the output stream
     */
    public void write(OutputStream out) throws IOException
    {
        int buffer = 0;
        int fill = 0;
        for (int i = 0; i < bytes.length; i++)
        {
            buffer = (buffer << 5) | Byte.toUnsignedInt(bytes[i]);
            fill += 5;
            if (fill >= 8)
            {
                fill -= 8;
                out.write((buffer >>> fill) & 0xFF);
            }
            
            if (bytes[i] == 0)
                break;
        }
        if (fill > 0)
            out.write((buffer & ((1 << fill) - 1)) << (8 - fill));
    }
    
    /**
     * Converts this ITA-2 string to UTF-8 string (java String).
     * 
     * @return UTF-8 string equal to this ITA-2 string.
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        
        boolean shift = false;
        for (int i = 0; i < bytes.length; i++)
        {
            char c = (char) CHARS_ITA2[bytes[i] | (shift ? CHARDESC_SHIFTON : CHARDESC_SHIFTOFF)];
            if (c == 0) break;
            else if (c == 14) shift = true;
            else if (c == 15) shift = false;
            else builder.append(c);
        }
        
        return builder.toString();
    }
    
    /**
     * Compares this ITA-2 string to another ITA-2 or UTF-8 string.
     * When comparing ITA-2 string to UTF-8 string, the ITA-2 string is first
     * converted to UTF-8, then the two UTF-8 strings are compared.
     * 
     * @param obj - reference to another ITA-2 or UTF-8 string with which to compare
     * @return true if the two strings are equal, otherwise false.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof String)
        {
            return toString().equals(obj);
        }
        else if (obj instanceof StringITA2)
        {
            StringITA2 str = (StringITA2) obj;
            return Arrays.equals(bytes, str.bytes);
        }
        return false;
    }
}
