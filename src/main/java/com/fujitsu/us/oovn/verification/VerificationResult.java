package com.fujitsu.us.oovn.verification;

/**
 * Currently only shows a message 
 * TODO: return alternative topology if fails
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class VerificationResult
{
    private final boolean _pass;
    private final String  _message;
    
    public VerificationResult(boolean pass, String msg)
    {
        _pass    = pass;
        _message = msg;
    }

    public boolean isPassed() {
        return _pass;
    }
    
    public String getMessage() {
        return _message;
    }
    
}