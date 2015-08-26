package com.potevio.sdtv.domain;

public class MapXY {
	// Jingdu, longitude
	private String x;
	// weidu ,latitude
	private String y;

	public MapXY() {
		// TODO Auto-generated constructor stub
	}
	public MapXY(String longitude,String latitude) {
		this.x=longitude;
		this.y=latitude;
	}
	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}
}
