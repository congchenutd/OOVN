package com.fujitsu.us.oovn.element;

import com.google.gson.JsonElement;

public interface Jsonable {
    public JsonElement toJson();
//    public boolean fromJson(JsonObject jsonObj);
}
