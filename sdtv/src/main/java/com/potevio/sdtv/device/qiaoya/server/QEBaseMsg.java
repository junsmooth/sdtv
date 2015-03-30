package com.potevio.sdtv.device.qiaoya.server;

import java.util.ArrayList;
import java.util.List;

public class QEBaseMsg {
	// protocol version V1.0.0
	private String ver = "V1.0.0";
	// sn of tcp package
	private String packSN;

	private int encryptType;
	private String encryptValue;
	private String imei;
	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	private String dateTime;
	private String msgCode;

	private List<String> paramsList;

	@Override
	public String toString() {
		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append("[");
		sbBuffer.append(getVer()).append(",");
		sbBuffer.append(getPackSN()).append(",");
		sbBuffer.append(getEncryptType()).append(",");
		sbBuffer.append(getEncryptValue()).append(",");
		sbBuffer.append(getDateTime()).append(",");
		sbBuffer.append(getImei()).append(",");
		sbBuffer.append(getMsgCode()).append(",");
		for (String str : getParamsList()) {
			sbBuffer.append(str).append(",");
		}
		sbBuffer.deleteCharAt(sbBuffer.lastIndexOf(","));
		sbBuffer.append("]");
		return sbBuffer.toString();
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getPackSN() {
		return packSN;
	}

	public void setPackSN(String packSN) {
		this.packSN = packSN;
	}

	public int getEncryptType() {
		return encryptType;
	}

	public void setEncryptType(int encryptType) {
		this.encryptType = encryptType;
	}

	public String getEncryptValue() {
		return encryptValue;
	}

	public void setEncryptValue(String encryptValue) {
		this.encryptValue = encryptValue;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public List<String> getParamsList() {
		if (paramsList == null) {
			paramsList = new ArrayList<String>();
		}
		return paramsList;
	}

	public void setParamsList(List<String> paramsList) {
		this.paramsList = paramsList;
	}
}
