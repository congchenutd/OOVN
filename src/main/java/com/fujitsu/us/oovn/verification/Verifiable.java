package com.fujitsu.us.oovn.verification;

import com.fujitsu.us.oovn.core.VNO;

/**
 * An interface for VNO verifiers
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public interface Verifiable {
    
    public VerificationResult verify(VNO vno);

}
