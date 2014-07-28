package com.fujitsu.us.oovn.core;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.is;

import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.exception.InvalidVNOOperationException;

public class VNOTest
{
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        PhysicalNetwork.init("PhysicalConfig.json");
    }
    
    /**
     * Test the state transition of a VNO
     * Any invalid operation should trigger a InvalidVNOOperationException
     */
    @Test
    public void testState() throws InvalidVNOOperationException
    {
        // unconfigured
        VNO vno = new VNO(new Tenant("Carl"));
        assertThat(vno.getState(), is(VNO.VNOState.UNCONFIGURED));
        
        // should init first
        try
        {
            vno.verify();
            fail("Expected an InvalidVNOOperationException to be thrown");
        } catch (InvalidVNOOperationException e) {
            assertThat(e.getMessage(), is("The VNO is not initialized (configured) yet"));
        }
        
        // failed init
        try {
            vno.init("NoSuchFile.json");
            fail("Expected an NoSuchFileException to be thrown");
        } catch (IOException e) {
            assertThat(vno.getState(), is(VNO.VNOState.UNCONFIGURED));
        }
        
        // successful init
        try {
            vno.init("VirtualConfig1.json");
        } catch(Exception e) {
        }
        assertThat(vno.getState(), is(VNO.VNOState.UNVERIFIED));
        
        // should verify() before activate()
        try
        {
            vno.activate();
            fail("Expected an InvalidVNOOperationException to be thrown");
        } catch (InvalidVNOOperationException e) {
            assertThat(e.getMessage(), is("The VNO is not verified yet"));
        }
        
        // should pass verification
        vno.verify();
        assertThat(vno.getState(), is(VNO.VNOState.INACTIVE));
        
        // cannot deactivate a non-active VNO
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
