package com.potevio.sdtv.device.ythtjr.android;

public class Bed implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4083495083298332210L;

	private Integer id;
	// 床号
	private String bedNum;

	public String getBedNum() {
		return bedNum;
	}

	public void setBedNum(String bedNum) {
		this.bedNum = bedNum;
	}

	private String seriesid;
	private String bindStatus;
	private String bedStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSeriesid() {
		return seriesid;
	}

	public void setSeriesid(String seriesid) {
		this.seriesid = seriesid;
	}

	public String getBindStatus() {
		return bindStatus;
	}

	public void setBindStatus(String bindStatus) {
		this.bindStatus = bindStatus;
	}

	public String getBedStatus() {
		return bedStatus;
	}

	public void setBedStatus(String bedStatus) {
		this.bedStatus = bedStatus;
	}

}
