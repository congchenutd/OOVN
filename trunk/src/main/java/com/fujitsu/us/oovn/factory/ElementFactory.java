package com.fujitsu.us.oovn.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;

public abstract class ElementFactory
{
    public Persistable createElement(Node node)
    {
        if(node.hasLabel(DynamicLabel.label("ZPhysical")))
            return fromNode(node, null);
        return null;
    }
    
    private static final Map<TreeSet<String>, ElementFactory> _elements 
                        = new HashMap<TreeSet<String>, ElementFactory>();
    
    public static void registerElement(TreeSet<String> labels, ElementFactory factory) {
        _elements.put(labels, factory);
    }
    
    public static Persistable fromNode(Node node, VNO vno)
    {
        TreeSet<String> sortedLabels = new TreeSet<String>();
        for(Label label: node.getLabels())
            sortedLabels.add(label.toString());
        
        if(_elements.containsKey(sortedLabels))
        {
            ElementFactory factory = _elements.get(sortedLabels);
            factory.create(node, vno);
        }
        
        return null;
    }
    
    protected abstract Persistable create(Node node, VNO vno);

}
