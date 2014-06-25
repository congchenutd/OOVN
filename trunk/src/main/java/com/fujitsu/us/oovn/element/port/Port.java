package com.fujitsu.us.oovn.element.port;

import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.datapath.Switch;
import com.fujitsu.us.oovn.element.link.Link;
import com.fujitsu.us.oovn.element.link.LinkPair;
import com.fujitsu.us.oovn.util.NoJson;


public class Port
{
    private int        _number;
    private MACAddress _mac;
    private Boolean    _isEdge;
    
    @NoJson
    private Switch     _switch;
    
    @NoJson
    private LinkPair   _linkPair;

    public Port(final int number) {
        setNumber(number);
    }
    
    public Switch getSwitch() {
        return _switch;
    }
    
    public void setSwitch(Switch sw) {
        _switch = sw;
    }
    
    public int getNumber() {
        return _number;
    }
    
    public void setNumber(int number) {
        _number = number;
    }

    public MACAddress getMAC() {
        return _mac;
    }

    public void setMAC(MACAddress mac) {
        _mac = mac;
    }

    public Boolean isEdge() {
        return _isEdge;
    }

    public void setIsEdge(Boolean isEdge) {
        _isEdge = isEdge;
    }

    public LinkPair getLinkPair() {
        return _linkPair;
    }

    public void setLinkPair(LinkPair linkPair) {
        _linkPair = linkPair;
    }
    
    public Link getInLink() {
        return getLinkPair().getInLink();
    }
    
    public Link getOutLink() {
        return getLinkPair().getOutLink();
    }

    @Override
    public String toString() {
        return  "PORT:" +
                "\n- number: "      + getNumber() + 
                "\n- wwitch: "      + getSwitch().getName() +
                "\n- MACAddress: "  + getMAC() +
                "\n- isEdge: "      + isEdge();
    }
    
    public boolean equals(Port other)
    {
        if(this == other)
            return true;
        
        if(!super.equals(other))
            return false;

        final Switch mySwitch   = getSwitch();
        final Switch yourSwitch = other.getSwitch();
        return  mySwitch == null && yourSwitch == null || 
                mySwitch != null && yourSwitch != null && mySwitch.equals(yourSwitch);
    }
}
