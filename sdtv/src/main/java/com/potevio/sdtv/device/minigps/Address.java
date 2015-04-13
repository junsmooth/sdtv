package com.potevio.sdtv.device.minigps;

public class Address {

	private String country;
	private String city;
	private String region;
	private String street;
	private String street_number;
	private String postal_code;
	private String county;
	private String country_code;

	@Override
	public String toString() {
		return "Address [country=" + country + ", city=" + city + ", region="
				+ region + ", street=" + street + ", street_number="
				+ street_number + ", postal_code=" + postal_code + ", county="
				+ county + ", country_code=" + country_code + "]";
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreet_number() {
		return street_number;
	}

	public void setStreet_number(String street_number) {
		this.street_number = street_number;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

}
