package com.potevio.sdtv.device.qiaoya.server;

import org.apache.mina.core.session.IoSession;

public class QEClientMsg extends QEBaseMsg {
	// terminal type: 1-1 1-2 ...
	private String terminalType;
	// watch imei
	private String imei;
	// time zone +8
	private String zone;

	private IoSession session;

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	private String msgCode;

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

}
