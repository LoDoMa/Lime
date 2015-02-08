package net.kalinovcic.libxbf.xbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.kalinovcic.libxbf.commons.StringITA2;

public class XBTNamedContainer extends XBTContainer
{
    public XBTNamedContainer()
    {
        
    }
    
    /**
     * Exactly the same as the addElement method from XBTContainer, but with an additional
     * check. Throws IllegalArgumentException is the element's name is equal to null
     * (if it is unnamed).<br>
     * 
     * @param element
     */
    @Override
    public void addElement(XBTElement element)
    {
        super.addElement(element);
        if (element.name == null)
            throw new IllegalArgumentException("unnamed element");
    }
    
    /**
     * Returns the index of the first element in the container with a specified name.<br>
     * Returns -1 if the element with such name doesn't exist.<br>
     * Complexity: O(N)<br>
     * <br>
     * 
     * @param name - the name of the element to find
     * @return the index of the first element in the container with a specified name
     */
    public int findElement(String name)
    {
        int index = 0;
        XBTElement element = list;
        while (element != null)
        {
            if (element.name.equals(name))
                return index;
            element = element.next;
            index++;
        }
        return -1;
    }

    /**
     * Returns an array of indices of all elements in the container with a specified name.<br>
     * Returns -1 if the element with such name doesn't exist.<br>
     * Complexity: O(N)<br>
     * <br>
     * 
     * @param name - the name of the elements to find
     * @return an array of indices
     */
    public int[] findElements(String name)
    {
        List<Integer> indexList = new ArrayList<Integer>();
        int index = 0;
        XBTElement element = list;
        while (element != null)
        {
            if (element.name.equals(name))
                indexList.add(index);
            element = element.next;
            index++;
        }
        
        int[] indices = new int[indexList.size()];
        for (int i = 0; i < indices.length; i++)
            indices[i] = indexList.get(i);
        return indices;
    }
    
    @Override
    void read(DataInputStream dis) throws IOException
    {
        byte type;
        while ((type = dis.readByte()) != 0)
        {
            String name = new StringITA2(dis).toString();
            switch (type)
            {
            case 1: addElement(new XBTContainer().name(name)); break;
            case 2: addElement(new XBTNamedContainer().name(name)); break;
            case 3: addElement(new XBTStringUTF8().name(name)); break;
            case 4: addElement(new XBTStringITA2().name(name)); break;
            case 5: addElement(new XBTArrayByte().name(name)); break;
            case 6: addElement(new XBTArrayShort().name(name)); break;
            case 7: addElement(new XBTArrayFloat().name(name)); break;
            case 8: addElement(new XBTByte().name(name)); break;
            case 9: addElement(new XBTShort().name(name)); break;
            case 10: addElement(new XBTInt().name(name)); break;
            case 11: addElement(new XBTLong().name(name)); break;
            case 12: addElement(new XBTFloat().name(name)); break;
            case 13: addElement(new XBTDouble().name(name)); break;
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
            else if (element instanceof XBTStringUTF8)      dos.writeByte(3);
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
            new StringITA2(element.name).write(dos);
            element.write(dos);
            element = element.next;
        }
        dos.writeByte(0);
    }
}
