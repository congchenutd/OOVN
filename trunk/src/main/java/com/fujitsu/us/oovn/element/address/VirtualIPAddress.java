package com.fujitsu.us.oovn.element.address;

public class VirtualIPAddress extends AbstractIPAddress
{
    private final int _tenantId;

    public VirtualIPAddress(final int tenantId, final int ip)
    {
        super();
        _tenantId = tenantId;
        _ip = ip;
    }

    public VirtualIPAddress(final int tenantId, final String ipString)
    {
        super(ipString);
        _tenantId = tenantId;
    }

    public int getTenantId() {
        return _tenantId;
    }
}
