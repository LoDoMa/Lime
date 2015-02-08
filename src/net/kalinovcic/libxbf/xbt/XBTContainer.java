package net.kalinovcic.libxbf.xbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class XBTContainer extends XBTElement
{
    XBTElement list;
    XBTElement last;
    
    public XBTContainer()
    {
        
    }
    
    /**
     * Adds a new element as the last element in the container.<br>
     * A NullPointerException is thrown if the argument is null.<br>
     * Complexity: O(1)<br>
     * <br>
     * 
     * @param element
     */
    public void addElement(XBTElement element)
    {
        if (element == null)
            throw new NullPointerException();
        
        if (list == null)
        {
            list = element;
            last = list;
        }
        else
        {
            last.next = element;
            last = element;
        }
    }
    
    /**
     * Returns an element in the container at a given index.<br>
     * An ArrayIndexOutOfBoundsException is thrown if the index is negative
     * or if it is greater than the element count.<br>
     * Complexity: O(N)<br>
     * <br>
     * 
     * @param index
     * @return an element from the container at a given index
     */
    public XBTElement getElement(int index)
    {
        if (list == null || index < 0)
            throw new ArrayIndexOutOfBoundsException(index);
        XBTElement element = list;
        while (index-- != 0)
        {
            element = element.next;
            if (element == null)
                throw new ArrayIndexOutOfBoundsException(index);
        }
        return element;
    }

    /**
     * Removes an element in the container at a given index.<br>
     * An ArrayIndexOutOfBoundsException is thrown if the index is negative
     * or if it is greater than the element count.<br>
     * Complexity: O(N)<br>
     * <br>
     * 
     * @param index
     */
    public void removeElement(int index)
    {
        if (list == null || index < 0)
            throw new ArrayIndexOutOfBoundsException(index);
        if (index == 0)
            list = list.next;
        else
        {
            XBTElement element = list;
            while (index-- - 1 != 0)
            {
                element = element.next;
                if (element == null)
                    throw new ArrayIndexOutOfBoundsException(index);
            }
            if (element.next == null)
                throw new ArrayIndexOutOfBoundsException(index);
            element.next = element.next.next;
        }
    }
    
    @Override
    void read(DataInputStream dis) throws IOException
    {
        byte type;
        while ((type = dis.readByte()) != 0)
        {
            switch (type)
            {
            case 1: addElement(new XBTContainer()); break;
            case 2: addElement(new XBTNamedContainer()); break;
            case 3: addElement(new XBTStringUTF8()); break;
            case 4: addElement(new XBTStringITA2()); break;
            case 5: addElement(new XBTArrayByte()); break;
            case 6: addElement(new XBTArrayShort()); break;
            case 7: addElement(new XBTArrayFloat()); break;
            case 8: addElement(new XBTByte()); break;
            case 9: addElement(new XBTShort()); break;
            case 10: addElement(new XBTInt()); break;
            case 11: addElement(new XBTLong()); break;
            case 12: addElement(new XBTFloat()); break;
            case 13: addElement(new XBTDouble()); break;
            default: throw new UnknownMemberException("illegal member ID " + type + " for current XBT version");
            }
            last.read(dis);
        }
    }
    
    @Override
    void write(DataOutputStream dos) throws IOException
    {
        XBTElement element = list;
        while (element != null)
        {
                 if (element instanceof XBTContainer)      dos.writeByte(1);
            else if (element instanceof XBTNamedContainer) dos.writeByte(2);
            else if (element instanceof XBTStringUTF8)     dos.writeByte(3);
            else if (element instanceof XBTStringITA2)     dos.writeByte(4);
            else if (element instanceof XBTArrayByte)      dos.writeByte(5);
            else if (element instanceof XBTArrayShort)     dos.writeByte(6);
            else if (element instanceof XBTArrayFloat)     dos.writeByte(7);
            else if (element instanceof XBTByte)           dos.writeByte(8);
            else if (element instanceof XBTShort)          dos.writeByte(9);
            else if (element instanceof XBTInt)            dos.writeByte(10);
            else if (element instanceof XBTLong)           dos.writeByte(11);
            else if (element instanceof XBTFloat)          dos.writeByte(12);
            else if (element instanceof XBTDouble)         dos.writeByte(13);
            else throw new UnknownMemberException("illegal member type " + element.getClass() + " for current XBT version");
            element.write(dos);
            list = list.next;
        }
        dos.writeByte(0);
    }
}
