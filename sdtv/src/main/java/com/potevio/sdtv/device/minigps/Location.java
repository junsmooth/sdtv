package com.potevio.sdtv.device.minigps;

public class Location {

	private Address address;
	private double longitude;
	private double latitude;
	private double accuracy;

	@Override
	public String toString() {
		return "Location [address=" + address + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", accuracy=" + accuracy + "]";
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

}
