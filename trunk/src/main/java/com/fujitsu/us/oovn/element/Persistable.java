package com.fujitsu.us.oovn.element;

public interface Persistable
{
    /**
     * @return a clause for matching the object 
     */
    public String toDBMatch();
    
    /**
     * @return a clause for creating the object
     */
    public String toDBCreate();
}
