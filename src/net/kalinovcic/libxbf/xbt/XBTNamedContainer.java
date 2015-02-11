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
    void read(DataInputStream dis, int version) throws IOException
    {
        byte type;
        while ((type = dis.readByte()) != 0)
        {
            String name = new StringITA2(dis).toString();
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
            new StringITA2(element.name).write(dos);
            element.write(dos, version);
            element = element.next;
        }
        dos.writeByte(0);
    }
}
