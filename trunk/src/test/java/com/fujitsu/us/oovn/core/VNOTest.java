package com.fujitsu.us.oovn.core;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.is;

import com.fujitsu.us.oovn.exception.InvalidVNOOperationException;

public class VNOTest
{

    @Test
    public void testState() throws InvalidVNOOperationException
    {
        VNO vno = new VNO(new Tenant("Carl"));
        assertThat(vno.getState(), is(VNO.VNOState.UNCONFIGURED));
        
        try
        {
            vno.verify();
            fail("Expected an InvalidVNOOperationException to be thrown");
        } catch (InvalidVNOOperationException e) {
            assertThat(e.getMessage(), is("The VNO is not initialized (configured) yet"));
        }
        
        vno.init("NoSuchFile.json");
        assertThat(vno.getState(), is(VNO.VNOState.UNCONFIGURED));
        
        vno.init("VirtualConfig.json");
        assertThat(vno.getState(), is(VNO.VNOState.UNVERIFIED));
        
        try
        {
            vno.activate();
            fail("Expected an InvalidVNOOperationException to be thrown");
        } catch (InvalidVNOOperationException e) {
            assertThat(e.getMessage(), is("The VNO is not verified yet"));
        }
        
        vno.verify();
        assertThat(vno.getState(), is(VNO.VNOState.INACTIVE));
        
        try
        {
            vno.deactivate();
            fail("Expected an InvalidVNOOperationException to be thrown");
        } catch (InvalidVNOOperationException e) {
            assertThat(e.getMessage(), is("The VNO is not activated yet"));
        }
        
        vno.activate();
        assertThat(vno.getState(), is(VNO.VNOState.ACTIVE));
        
        vno.deactivate();
        assertThat(vno.getState(), is(VNO.VNOState.INACTIVE));
        
        vno.decommission();
        assertThat(vno.getState(), is(VNO.VNOState.DECOMMISSIONED));
    }
    
}
