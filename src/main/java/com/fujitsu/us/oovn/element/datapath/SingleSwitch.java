package com.fujitsu.us.oovn.element.datapath;

import java.util.Arrays;
import java.util.List;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.factory.ElementFactory;
import com.fujitsu.us.oovn.factory.SingleSwitchFactory;
import com.fujitsu.us.oovn.map.MapBase;

/**
 * A SingleSwitch maps to one physical switch
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class SingleSwitch extends VirtualSwitch implements Persistable
{
    private PhysicalSwitch _physicalSwitch;
    
    static {
        ElementFactory.registerElement(SingleSwitch.class, new SingleSwitchFactory());
    }
    
    public SingleSwitch(VNO vno, DPID dpid, String name) {
        super(vno, dpid, name);
    }

    public PhysicalSwitch getPhysicalSwitch() {
        return _physicalSwitch;
    }
    
    public void setPhysicalSwitch(PhysicalSwitch physical) {
        _physicalSwitch = physical;
    }

    @Override
    public List<PhysicalSwitch> getPhysicalSwitches() {
        return Arrays.asList(getPhysicalSwitch());
    }

    @Override
    public String toDBMatch() {
        return  "(" + toDBVariable() +
                ":Virtual:Switch:Single {" +
                    "type:\"SingleSwitch\"," +
                    "vnoid:" + getVNO().getID() + "," +
                    "dpid:\"" + getDPID().toString() + "\"," +
                    "name:\"" + getName() + 
                "\"})";
    }

    @Override
    public void createMapping(MapBase map)
    {
        // nothing to map to
        if(getPhysicalSwitch() == null)
            return;
        
        map.query(
                "MATCH\n" +
                    toDBMatch() + ",\n" +
                    getPhysicalSwitch().toDBMatch() + "\n" +
                "CREATE\n" +
                    "(" + toDBVariable() + ")-[:Maps]->(" + 
                    getPhysicalSwitch().toDBVariable() + ")");
    }

}
