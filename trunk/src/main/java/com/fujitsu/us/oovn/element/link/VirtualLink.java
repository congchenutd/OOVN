package com.fujitsu.us.oovn.element.link;

import java.util.LinkedList;
import java.util.List;

import com.fujitsu.us.oovn.element.port.Port;

public class VirtualLink extends Link
{
    private final List<PhysicalLink> _path;
    
    public VirtualLink(Port src, Port dst)
    {
        super(src, dst);
        _path = new LinkedList<PhysicalLink>();
    }

    @Override
    public String toString()
    {
        
    }
    
}
