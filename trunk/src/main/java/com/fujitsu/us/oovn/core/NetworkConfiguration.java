package com.fujitsu.us.oovn.core;

import com.fujitsu.us.oovn.element.Jsonable;
import com.google.gson.JsonObject;

public class NetworkConfiguration implements Jsonable
{
    private JsonObject _json;
    private boolean    _verified;
    
    public NetworkConfiguration(JsonObject json)
    {
        _json     = json;
        _verified = false;
    }
    
    public boolean isVerified() {
        return _verified;
    }
    
    public void setVerified(boolean verified) {
        _verified = verified;
    }
    
    public void setJson(JsonObject json) {
        _json = json;
    }
    
    @Override
    public JsonObject toJson() {
        return _json;
    }
    
    @Override
    public String toString() {
        return _json.toString();
    }
}
