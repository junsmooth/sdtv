package com.potevio.sdtv.device.syshelp;

public class WatchMSG {
	public static final String DATATYPE_PUSE="pulse";
	public static final String DATATYPE_GPS="GPS_AT";
	private String mobile;
	private String datatype;
	private String pulsecount;
	private String longitude="0.000000";
	private String timen;
	public String getTimen() {
		return timen;
	}
	public void setTimen(String timen) {
		this.timen = timen;
	}
	@Override
	public String toString() {
		return "WatchMSG [mobile=" + mobile + ", datatype=" + datatype
				+ ", pulsecount=" + pulsecount + ", longitude=" + longitude
				+ ", timen=" + timen + ", latitude=" + latitude + ", LBS="
				+ LBS + ", height=" + height + ", speed=" + speed
				+ ", direction=" + direction + "]";
	}
	private String latitude="90.000000";
	private String LBS;
	private String height;
	private String speed;
	private String direction;
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getPulsecount() {
		return pulsecount;
	}
	public void setPulsecount(String pulsecount) {
		this.pulsecount = pulsecount;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLBS() {
		return LBS;
	}
	public void setLBS(String lBS) {
		LBS = lBS;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
}
