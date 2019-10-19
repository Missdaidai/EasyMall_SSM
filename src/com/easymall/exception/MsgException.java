package com.easymall.exception;

public class MsgException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public MsgException(){

    }
    public MsgException(String str) {
        super(str);
    }
}
