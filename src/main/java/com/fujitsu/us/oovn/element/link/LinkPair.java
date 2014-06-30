package com.fujitsu.us.oovn.element.link;

import com.fujitsu.us.oovn.element.Jsonable;
import com.fujitsu.us.oovn.element.port.Port;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class LinkPair implements Jsonable
{
    private Link _in;
    private Link _out;
    
    public LinkPair(Link in, Link out)
    {
        setInLink(in);
        setOutLink(out);
    }
    
    public LinkPair(Port port1, Port port2) {
        this(new Link(port1, port2), new Link(port2, port1));
    }
    
    public Link getInLink() {
        return _in;
    }
    
    public Link getOutLink() {
        return _out;
    }

    protected void setInLink(final Link in) {
        _in = in;
    }

    protected void setOutLink(final Link out) {
        _out = out;
    }
    
    @Override
    public String toString() {
        return "LinkPair[int:" + _in.toString() + " and out:" + _out.toString() + "]";
    }

    @Override
    public JsonElement toJson()
    {
        JsonObject result = new JsonObject();
        result.add("egress",  getOutLink().toJson());
        result.add("ingress", getInLink ().toJson());
        return result;
    }
    
}
