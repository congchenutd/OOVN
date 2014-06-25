package com.fujitsu.us.oovn.exception;

public class InvalidDPIDException extends IllegalArgumentException {

    private static final long serialVersionUID = 1825194551647799520L;

    public InvalidDPIDException() {
        super();
    }

    public InvalidDPIDException(final String msg) {
        super(msg);
    }
}