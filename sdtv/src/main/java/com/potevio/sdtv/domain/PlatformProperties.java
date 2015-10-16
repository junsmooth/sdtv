package com.potevio.sdtv.domain;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "platform", ignoreUnknownFields = false)
@Component
public class PlatformProperties {

	private String baseurl;
	private String bedaction;
	private String watchaction;
//	public String getBaseurl() {
//		return baseurl;
//	}
	
	
	public void setBaseurl(String baseurl) {
		this.baseurl = baseurl;
	}
	public String getBedaction() {
		return bedaction;
	}
	public void setBedaction(String bedaction) {
		this.bedaction = bedaction;
	}
	public String getWatchaction() {
		return watchaction;
	}
	public void setWatchaction(String watchaction) {
		this.watchaction = watchaction;
	}
	public String[] getBaseUrls() {
		return StringUtils.split(baseurl, ",");
	}

}
