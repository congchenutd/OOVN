package com.fujitsu.us.oovn.map;

import java.util.HashMap;
import java.util.Map;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.PhysicalIPAddress;
import com.fujitsu.us.oovn.element.address.VirtualIPAddress;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.datapath.VirtualSwitch;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.link.VirtualLink;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.element.port.VirtualPort;

public class LocalMap extends MapBase
{
    private final VNO                   _vno;
    private static Map<Integer, LocalMap> _maps = new HashMap<Integer, LocalMap>();
    
    /**
     * @param vno a VNO
     * @return the VNOMap for the vno
     */
    public static LocalMap getInstance(VNO vno)
    {
        if(!_maps.containsKey(vno.getID()))
            _maps.put(vno.getID(), new LocalMap(vno));
        return _maps.get(vno.getID());
    }
    
    private LocalMap(VNO vno)
    {
        super("LocalMapDB" + vno.getID());
        _vno = vno;
    }
    
    public void registerVNO() {
        super.registerVNO(_vno);
    }
    
    public void unregisterVNO() {
        super.unregisterVNO(_vno);
    }
    
    public void activateVNO() {
        super.activateVNO(_vno);
    }
    
    public void deactivateVNO() {
        super.deactivateVNO(_vno);
    }
    
    public VirtualSwitch getVirtualSwitch(PhysicalSwitch psw) {
        return super.getVirtualSwitch(psw, _vno);
    }
    
    public VirtualPort getVirtualPort(PhysicalPort pPort) {
        return super.getVirtualPort(pPort, _vno);
    }
    
    public VirtualLink getVirtualLink(PhysicalLink pLink) {
        return super.getVirtualLink(pLink, _vno);
    }
    
    public VirtualIPAddress getVirtualIPAddress(PhysicalIPAddress pIP) {
        return super.getVirtualIPAddress(pIP, _vno);
    }
}

