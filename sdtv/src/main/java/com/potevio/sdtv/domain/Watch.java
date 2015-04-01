package com.potevio.sdtv.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "t_watch")
public class Watch implements Serializable {
	private static final long serialVersionUID = 7249013529789428850L;
	public static final String DT_PULSE = "pulse";
	public static final String DT_GPS = "gps";
	public static final String DT_LBS = "lbs";
	public static final String DT_STEP = "step";
	public static final String VENDOR_QE = "QE";
	public static final String VENDOR_SYSHELP = "SYSHELP";

	private String longitude = "0.000000";
	private String latitude = "0.000000";
	private String height;
	private String speed;

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
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

	public void addLbs(LBS lbs) {

		if (lbsList == null) {
			synchronized (this) {
				if (lbsList == null) {
					lbsList = new ArrayList<LBS>();
				}
			}
		}
		lbsList.add(lbs);
	}

	@OneToMany(mappedBy = "watch", cascade = CascadeType.ALL)
	private List<LBS> lbsList;

	public List<LBS> getLbsList() {
		return lbsList;
	}

	public void setLbsList(List<LBS> lbsList) {
		this.lbsList = lbsList;
	}

	@Id
	@GeneratedValue
	private Long id;
	private String temperature;

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * PULSE,GPS,LBS,STEP
	 */
	private String dataType;
	private String imeiString;

	public String getImeiString() {
		return imeiString;
	}

	public void setImeiString(String imeiString) {
		this.imeiString = imeiString;
	}

	private String humidity;
	private String heartbeat;
	private String stepcount;
	@Column(name = "createdate")
	private Date createDate;
	private String gps;
	private String vendor;
	private Date dataTime;

	public Long getId() {
		return id;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Date getDataTime() {
		return dataTime;
	}

	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Watch [id=" + id + ", temperature=" + temperature
				+ ", humidity=" + humidity + ", heartbeat=" + heartbeat
				+ ", stepcount=" + stepcount + ", createdate=" + createDate
				+ "]";
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(String heartbeat) {
		this.heartbeat = heartbeat;
	}

	public String getStepcount() {
		return stepcount;
	}

	public void setStepcount(String stepcount) {
		this.stepcount = stepcount;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
