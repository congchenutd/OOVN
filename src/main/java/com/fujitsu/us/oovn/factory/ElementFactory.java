package com.fujitsu.us.oovn.factory;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
import com.google.gson.JsonObject;

public abstract class ElementFactory
{
    private static final Map<Class<? extends Persistable>, 
                             ElementFactory> _elements 
                        = new HashMap<Class<? extends Persistable>, 
                                      ElementFactory>();
    
    public static void registerElement(Class  <? extends Persistable> type,
                                       ElementFactory factory) {
        _elements.put(type, factory);
    }
    
//    public static Persistable fromNode(Node node, VNO vno)
//    {
////        String type = node.getProperty("type").toString();
////        
////        if(_elements.containsKey(type))
////        {
////            ElementFactory factory = _elements.get(type);
////            return factory.create(node, vno);
////        }
//        
//        return null;
//    }
    
    public static <T extends Persistable> T fromNode(Class<T> type, Node node, VNO vno)
    {
        try {
            Class t = Class.forName(node.getProperty("type").toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(_elements.containsKey(type))
        {
            ElementFactory factory = _elements.get(type);
            return (T) factory.create(node, vno);
        }
        
        return null;
    }
    
    public static <T extends Persistable> T fromNode(Class<T> type, Node node) {
        return fromNode(type, node, null);
    }
    
    public static Persistable fromJson(JsonObject json, VNO vno)
    {
        return null;
//        String type = json.get("type").getAsString();
//        ElementFactory factory = getFactory(type);
//        return factory == null ? null : factory.create(json, vno);
    }
    
    protected abstract Persistable create(Node node,       VNO vno);
    protected abstract Persistable create(JsonObject json, VNO vno);

}
