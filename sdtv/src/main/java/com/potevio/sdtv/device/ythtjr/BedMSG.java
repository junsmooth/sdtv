package com.potevio.sdtv.device.ythtjr;

public class BedMSG {
	private String deviceid;
	private String heartrating;
	private String resping;
	private String status;

	public String getDeviceid() {
		return deviceid;
	}

	@Override
	public String toString() {
		return "BedMSG [deviceid=" + deviceid + ", heartrating=" + heartrating
				+ ", resping=" + resping + ", status=" + status + "]";
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getHeartrating() {
		return heartrating;
	}

	public void setHeartrating(String heartrating) {
		this.heartrating = heartrating;
	}

	public String getResping() {
		return resping;
	}

	public void setResping(String resping) {
		this.resping = resping;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
