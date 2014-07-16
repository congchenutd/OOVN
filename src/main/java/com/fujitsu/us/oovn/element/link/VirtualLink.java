package com.fujitsu.us.oovn.element.link;

import java.util.LinkedList;
import java.util.List;

import com.fujitsu.us.oovn.element.port.Port;
import com.fujitsu.us.oovn.element.port.VirtualPort;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class VirtualLink extends Link
{
    private List<PhysicalLink> _path;
    
    public VirtualLink(Port src, Port dst)
    {
        super(src, dst);
        _path = new LinkedList<PhysicalLink>();
    }
    
    public void setPath(List<PhysicalLink> path)
    {
        if(path.isEmpty())
            return;
        
        if(path.get(0)            .getSrcPort().equals(((VirtualPort) getSrcPort()).getPhysicalPort()) && 
           path.get(path.size()-1).getDstPort().equals(((VirtualPort) getDstPort()).getPhysicalPort()))
           _path = path;
    }
    
    public List<PhysicalLink> getPath() {
        return _path;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("VirtualLink: ");
        for(Link link: getPath())
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
    
}
