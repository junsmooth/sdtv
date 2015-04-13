package com.potevio.sdtv.device.minigps;

public class CellTower {
	private String cell_id;
	private String location_area_code;
	private String mobile_country_code;
	private String mobile_network_code;
	private int age = 0;
	public CellTower(String mcc, String mnc, String lac, String cell) {

		this.mobile_country_code=mcc;
		this.mobile_network_code=mnc;
		this.location_area_code=lac;
		this.cell_id=cell;
	}
	public String getCell_id() {
		return cell_id;
	}
	public void setCell_id(String cell_id) {
		this.cell_id = cell_id;
	}
	public String getLocation_area_code() {
		return location_area_code;
	}
	public void setLocation_area_code(String location_area_code) {
		this.location_area_code = location_area_code;
	}
	public String getMobile_country_code() {
		return mobile_country_code;
	}
	public void setMobile_country_code(String mobile_country_code) {
		this.mobile_country_code = mobile_country_code;
	}
	public String getMobile_network_code() {
		return mobile_network_code;
	}
	public void setMobile_network_code(String mobile_network_code) {
		this.mobile_network_code = mobile_network_code;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getSignal_strength() {
		return signal_strength;
	}
	public void setSignal_strength(int signal_strength) {
		this.signal_strength = signal_strength;
	}
	private int signal_strength = -65;
}
