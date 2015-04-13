package com.potevio.sdtv.device.minigps;

public class MiniGPSResult {
	private Location location;

	public Location getLocation() {
		return location;
	}

	@Override
	public String toString() {
		return "GPSResult [location=" + location + "]";
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
}
