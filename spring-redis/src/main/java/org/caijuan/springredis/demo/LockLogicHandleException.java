package org.caijuan.springredis.demo;

public class LockLogicHandleException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public LockLogicHandleException(Throwable cause) {
	    super(cause);
	}
	 
	public LockLogicHandleException() {
		
	}
	
	public LockLogicHandleException(String msg) {
		super(msg);
	}
}