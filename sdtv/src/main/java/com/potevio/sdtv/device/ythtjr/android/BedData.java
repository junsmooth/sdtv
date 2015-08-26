//package com.potevio.sdtv.device.ythtjr.android;
//
//import java.util.Date;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//@Entity
//@Table(name = "t_beddata")
//public class BedData implements java.io.Serializable {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 564380556114486139L;
//	@Id
//	@GeneratedValue
//	private Long id;
//	private Bed bed;
//
//	public Bed getBed() {
//		return bed;
//	}
//
//	@Override
//	public String toString() {
//		return "BedData [id=" + id + ", bed=" + bed + ", bedSeriesId="
//				+ bedSeriesId + ", patName=" + patName + ", patCode=" + patCode
//				+ ", heartrate=" + heartrate + ", resp=" + resp + ", status="
//				+ status + ", dataTime=" + dataTime + "]";
//	}
//
//	public void setBed(Bed bed) {
//		this.bed = bed;
//	}
//
//	private String bedSeriesId;
//	private String patName;
//	private String patCode;
//
//	private int heartrate;
//	private int resp;
//	private String status;
//	private String statusCode;
//
//	public String getStatusCode() {
//		return statusCode;
//	}
//
//	public void setStatusCode(String statusCode) {
//		this.statusCode = statusCode;
//	}
//
//	private Date dataTime;
//
//	public String getBedSeriesId() {
//		return bedSeriesId;
//	}
//
//	public void setBedSeriesId(String bedSeriesId) {
//		this.bedSeriesId = bedSeriesId;
//	}
//
//	public String getPatName() {
//		return patName;
//	}
//
//	public void setPatName(String patName) {
//		this.patName = patName;
//	}
//
//	public String getPatCode() {
//		return patCode;
//	}
//
//	public void setPatCode(String patCode) {
//		this.patCode = patCode;
//	}
//
//	public Date getDataTime() {
//		return dataTime;
//	}
//
//	public void setDataTime(Date dataTime) {
//		this.dataTime = dataTime;
//	}
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public int getHeartrate() {
//		return heartrate;
//	}
//
//	public void setHeartrate(int heartrate) {
//		this.heartrate = heartrate;
//	}
//
//	public int getResp() {
//		return resp;
//	}
//
//	public void setResp(int resp) {
//		this.resp = resp;
//	}
//
//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}
//}
