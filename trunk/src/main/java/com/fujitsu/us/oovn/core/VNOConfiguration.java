package com.fujitsu.us.oovn.core;

/**
 * A description of VNO configuration
 * Loads configuration info from a JSon string
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class VNOConfiguration
{
    public int id;
    public String ip;
    // swithes, ports, links, hosts
    
    public static VNOConfiguration fromJson(String jsonString)
    {
        VNOConfiguration result = new VNOConfiguration();
        return result;
    }
}
