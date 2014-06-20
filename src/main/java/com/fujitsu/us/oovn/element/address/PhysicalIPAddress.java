package com.fujitsu.us.oovn.element.address;

public class PhysicalIPAddress extends AbstractIPAddress
{
    public PhysicalIPAddress(final Integer ip) {
        _ip = ip;
    }

    public PhysicalIPAddress(final String ipAddress) {
        super(ipAddress);
    }

//    public Integer getTenantId() {
//        return ip >> (32 - OpenVirteXController.getInstance()
//                .getNumberVirtualNets());
//    }
}
