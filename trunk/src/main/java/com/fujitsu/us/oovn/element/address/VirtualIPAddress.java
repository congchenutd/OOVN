package com.fujitsu.us.oovn.element.address;

public class VirtualIPAddress extends IPAddress
{
    private final int _tenantId;

    public VirtualIPAddress(final int tenantId, final String ipString)
    {
        super(ipString);
        _tenantId = tenantId;
    }

    public int getTenantId() {
        return _tenantId;
    }
}
