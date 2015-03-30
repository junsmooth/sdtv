package com.potevio.sdtv.device.qiaoya;

public class ResultCode {

	private static ResultCode SUCCESS = new ResultCode(true, "");

	public static ResultCode getSuccessMsg() {
		return SUCCESS;
	}

	private boolean success;
	private String failReason;

	public ResultCode() {
	}

	public ResultCode(boolean success, String msg) {
		this.success = success;
		this.failReason = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

}
