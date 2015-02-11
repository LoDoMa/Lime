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
    void read(DataInputStream dis, int version) throws IOException
    {
        byte type;
        while ((type = dis.readByte()) != 0)
        {
            XBTElement newMember = null;
            
            switch (type)
            {
            case 1: if (version >= XBT.VERSION_100) newMember = new XBTContainer(); break;
            case 2: if (version >= XBT.VERSION_100) newMember = new XBTNamedContainer(); break;
            case 3: if (version >= XBT.VERSION_100) newMember = new XBTStringUTF8(); break;
            case 4: if (version >= XBT.VERSION_100) newMember = new XBTStringITA2(); break;
            case 5: if (version >= XBT.VERSION_100) newMember = new XBTArrayByte(); break;
            case 6: if (version >= XBT.VERSION_100) newMember = new XBTArrayShort(); break;
            case 7: if (version >= XBT.VERSION_100) newMember = new XBTArrayFloat(); break;
            case 8: if (version >= XBT.VERSION_100) newMember = new XBTByte(); break;
            case 9: if (version >= XBT.VERSION_100) newMember = new XBTShort(); break;
            case 10: if (version >= XBT.VERSION_100) newMember = new XBTInt(); break;
            case 11: if (version >= XBT.VERSION_100) newMember = new XBTLong(); break;
            case 12: if (version >= XBT.VERSION_100) newMember = new XBTFloat(); break;
            case 13: if (version >= XBT.VERSION_100) newMember = new XBTDouble(); break;
            case 14: if (version >= XBT.VERSION_103) newMember = new XBTArrayInt(); break;
            }
            
            if (newMember == null)
                throw new UnknownMemberException("illegal member ID " + type + " for XBT version " + XBT.VERSION_NAMEMAP[version]);
            
            addElement(newMember.name(name));
            newMember.read(dis, version);
        }
    }
    
    @Override
    void write(DataOutputStream dos, int version) throws IOException
    {
        XBTElement element = list;
        while (element != null)
        {
            int type = 0;
                 if (element instanceof XBTContainer)      { if (version >= XBT.VERSION_100) type = 1; }
            else if (element instanceof XBTNamedContainer) { if (version >= XBT.VERSION_100) type = 2; }
            else if (element instanceof XBTStringUTF8)     { if (version >= XBT.VERSION_100) type = 3; }
            else if (element instanceof XBTStringITA2)     { if (version >= XBT.VERSION_100) type = 4; }
            else if (element instanceof XBTArrayByte)      { if (version >= XBT.VERSION_100) type = 5; }
            else if (element instanceof XBTArrayShort)     { if (version >= XBT.VERSION_100) type = 6; }
            else if (element instanceof XBTArrayFloat)     { if (version >= XBT.VERSION_100) type = 7; }
            else if (element instanceof XBTByte)           { if (version >= XBT.VERSION_100) type = 8; }
            else if (element instanceof XBTShort)          { if (version >= XBT.VERSION_100) type = 9; }
            else if (element instanceof XBTInt)            { if (version >= XBT.VERSION_100) type = 10; }
            else if (element instanceof XBTLong)           { if (version >= XBT.VERSION_100) type = 11; }
            else if (element instanceof XBTFloat)          { if (version >= XBT.VERSION_100) type = 12; }
            else if (element instanceof XBTDouble)         { if (version >= XBT.VERSION_100) type = 13; }
            else if (element instanceof XBTArrayInt)       { if (version >= XBT.VERSION_103) type = 14; }
            
            if (type == 0)
                throw new UnknownMemberException("illegal member type " + element.getClass() + " for XBT version " + XBT.VERSION_NAMEMAP[version]);

            dos.writeByte(type);
            element.write(dos, version);
            element = element.next;
        }
        dos.writeByte(0);
    }
}
