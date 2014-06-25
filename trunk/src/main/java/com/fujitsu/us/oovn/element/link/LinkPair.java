package com.fujitsu.us.oovn.element.link;

public class LinkPair
{
    private Link _in;
    private Link _out;
    
    public LinkPair(final Link in, final Link out)
    {
        setInLink(in);
        setOutLink(out);
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
    
}
