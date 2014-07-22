package com.fujitsu.us.oovn.element;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;

public class NodeFactory {

    public static Persistable fromNode(Node node)
    {
        if(node.hasLabel(DynamicLabel.label("Switch")))
        {
            if(node.hasLabel(DynamicLabel.label("Physical")))
                return PhysicalSwitch.fromNode(node);
        }
        return null;
    }

}
