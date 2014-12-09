package com.potevio.sdtv.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "t_watch")
public class Watch implements Serializable {
	private static final long serialVersionUID = 7249013529789428850L;
	@Id
	@GeneratedValue
	private Long id;
	private String temperature;

	private String humidity;
	private String heartbeat;
	private String stepcount;
	@Column(name = "createdate")
	private Date createDate;

	public Long getId() {
		return id;
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
