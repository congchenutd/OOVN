package com.fujitsu.us.oovn.verification;

import com.fujitsu.us.oovn.core.VNO;

/**
 * The base class for the verification chain (chain of responsibility DP)
 * 
 * Each concrete Verifier class should implement verify() in the following way:
 * 1. do its own verification
 * 2. if failed, return immediately 
 *    (TODO: or add error info and return by the end of the chain)
 * 3. otherwise, return super.verify() to continue the chain until the end
 * 
 * The client should use the verifiers in this way:
 * Verifier v = new VerifierA(new VerifierB(new VerifierC(null));
 * VerificationResult r = v.verify(vno);
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class Verifier implements Verifiable
{

    private final Verifier _next;
    
    public Verifier(Verifier next) {
        _next = next;
    }

    @Override
    public VerificationResult verify(VNO vno) {
        return _next != null ? _next.verify(vno) 
                             : new VerificationResult(true, "Pass");
    }

}
