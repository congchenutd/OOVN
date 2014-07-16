package com.fujitsu.us.oovn.element.link;

import java.util.LinkedList;
import java.util.List;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.datapath.VirtualSwitch;
import com.fujitsu.us.oovn.element.port.VirtualPort;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class VirtualLink extends Link<VirtualSwitch, VirtualPort> implements Persistable
{
    private final VNO          _vno;
    private List<PhysicalLink> _path;
    
    public VirtualLink(VNO vno, VirtualPort src, VirtualPort dst)
    {
        super(src, dst);
        _vno  = vno;
        _path = new LinkedList<PhysicalLink>();
    }
    
    public void setPath(List<PhysicalLink> path)
    {
        if(path.isEmpty())
            return;
        
        if(path.get(0)            .getSrcPort().equals(getSrcPort().getPhysicalPort()) && 
           path.get(path.size()-1).getDstPort().equals(getDstPort().getPhysicalPort()))
           _path = path;
    }
    
    public List<PhysicalLink> getPath() {
        return _path;
    }
    
    public VNO getVNO() {
        return _vno;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("VirtualLink: ");
        for(PhysicalLink link: getPath())
            builder.append(link.getName() + ",");
        return builder.toString();
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = (JsonObject) super.toJson();
        if(!getPath().isEmpty())
        {
            JsonArray path = new JsonArray();
            for(PhysicalLink link: getPath())
                path.add(link.toJson());
            result.add("path", path);
        }
        return result;
    }
    
    @Override
    public String toDBMatch() {
        return  getName() + 
                ":Virtual:Link " + "{" + 
                "vnoid:" + getVNO().getID() + "," +
                "srcSwitch:" + "\"" + getSrcSwitch().getDPID().toString() + "\", " +
                "srcPort:" + getSrcPort().getNumber() + "," +
                "dstSwitch:" + "\"" + getDstSwitch().getDPID().toString() + "\", " +
                "dstPort:" + getDstPort().getNumber() +
                "}";
    }
    
    @Override
    public String toDBCreate() {
        return "(" + toDBMatch() + ")";
    }
}
