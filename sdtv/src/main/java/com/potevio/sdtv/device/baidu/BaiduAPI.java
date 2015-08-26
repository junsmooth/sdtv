package com.potevio.sdtv.device.baidu;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;

import com.potevio.sdtv.domain.MapXY;

public class BaiduAPI {

	private static final String url="http://api.map.baidu.com/geoconv/v1/?";
	public static MapXY transferToBDMap(MapXY pointMapXY) {
		// http://api.map.baidu.com/geoconv/v1/?coords=114.21892734521,29.575429778924;114.21892734521,29.575429778924&from=1&to=5&ak=30b8356b2e5d2693e2391f3e39aef2ad
		String coordsString = pointMapXY.getX() + "," + pointMapXY.getY();
		String urlString = url + "coords=" + coordsString
				+ "&from=3&to=5&ak=30b8356b2e5d2693e2391f3e39aef2ad";
		try {
			String resultString = Request.Get(urlString).execute()
					.returnContent().asString();
			System.out.println(resultString);
			String xString = StringUtils.substringBetween(resultString, "x\":",
					",");
			System.out.println(xString);
			String yString = StringUtils.substringAfterLast(resultString,
					"y\":");
			yString = StringUtils.substringBefore(yString, "}");
			System.out.println(yString);
			pointMapXY.setX(xString);
			pointMapXY.setY(yString);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pointMapXY;
	}
}
