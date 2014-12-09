package com.potevio.sdtv.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "t_bed")
public class Bed {
	@Id
	@Column(name = "BED_ID")
	private int id;
	@Column(name = "PAT_NAME")
	private String patname;
	@Column(name = "DATA_DATE")
	private Date occurTime;
	private int status;
	private int heartrating;
	private int resping;
	@Transient
	private int warn;

	public int getWarn() {
		return warn;
	}

	public void setWarn(int warn) {
		this.warn = warn;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPatname() {
		return patname;
	}

	public void setPatname(String patname) {
		this.patname = patname;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getOccurTime() {
		return occurTime;
	}

	public void setOccurTime(Date occurTime) {
		this.occurTime = occurTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getHeartrating() {
		return heartrating;
	}

	public void setHeartrating(int heartrating) {
		this.heartrating = heartrating;
	}

	public int getResping() {
		return resping;
	}

	public void setResping(int resping) {
		this.resping = resping;
	}
}
