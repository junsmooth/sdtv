package com.potevio.sdtv.device.qiaoya.server.processor;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.potevio.sdtv.device.baidu.BaiduAPI;
import com.potevio.sdtv.device.minigps.CellTower;
import com.potevio.sdtv.device.minigps.MiniGPS;
import com.potevio.sdtv.device.minigps.MiniGPSRequest;
import com.potevio.sdtv.device.minigps.MiniGPSResult;
import com.potevio.sdtv.device.qiaoya.server.QEClientMsg;
import com.potevio.sdtv.device.qiaoya.server.Util;
import com.potevio.sdtv.domain.LBS;
import com.potevio.sdtv.domain.Watch;
import com.potevio.sdtv.service.MapXY;
import com.potevio.sdtv.service.WatchService;
import com.potevio.sdtv.util.CacheUtil;
import com.potevio.sdtv.util.DateUtil;

/**
 * GPS data
 * 
 * @author sdtv
 *
 */
@Component
public class T29 extends AbstractRequestMsg {
	private static final Logger logger = LoggerFactory.getLogger(T29.class);
	private String returnMsgCode = "S29";

	@Autowired
	private WatchService watchService;

	@Override
	public void process(QEClientMsg msg) {
		try {
			Util.sendReturnMessage(msg, returnMsgCode);
			List<String> paramsList = msg.getParamsList();
			String dataType = paramsList.get(0);
			String longitude = paramsList.get(1);
			String latitude = paramsList.get(2);
			// gcj02 -to baidu
			String lbsString = paramsList.get(3);
			String height = paramsList.get(4);
			String speed = paramsList.get(5);
			String direction = paramsList.get(6);
			String timen = paramsList.get(7);

			Watch watch = new Watch();
			watch.setVendor(Watch.VENDOR_QE);
			watch.setImeiString(msg.getImei());
			watch.setCreateDate(new Date());
			watch.setDataTime(DateUtil.string2Date(msg.getDateTime()));
			watch.setDataType(Watch.DT_GPS);
			watch.setLongitude(longitude);
			watch.setLatitude(latitude);

			// parse lbs string
			if (lbsString.contains("mcount")) {
				// multi lbs

				// mcount$count$mcc$mnc$response#lac|cell|power|level#lac2|cell2|...
				String headString = StringUtils.substringBefore(lbsString, "#");
				String parseString = StringUtils
						.substringAfter(headString, "$");
				String[] arr = StringUtils.split(parseString, "$");
				int num = Integer.parseInt(arr[0]);
				String mcc = arr[1];
				String mnc = arr[2];
				String responseTime = arr[3];
				String[] bodyArr = StringUtils.substringAfter(lbsString, "#")
						.split("#");
				for (int i = 0; i < num; i++) {
					try {
						LBS lbs = new LBS();
						lbs.setMcc(mcc);
						lbs.setMnc(mnc);
						String[] lbsArr = StringUtils.split(bodyArr[i], "|");

						String lac = Integer.parseInt(lbsArr[0], 16) + "";

						String cell = Integer.parseInt(lbsArr[1], 16) + "";

						String power = lbsArr[2];

						String level = lbsArr[3];

						lbs.setLac(lac);
						lbs.setCell(cell);
						lbs.setPower(Integer.parseInt(power));
						lbs.setLevel(Integer.parseInt(level));
						lbs.setWatch(watch);
						watch.addLbs(lbs);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} else {
				// single lbs
				// count$0$1|1111|xxxx

				String str = StringUtils.substringBetween(lbsString, "$", "|");
				String[] arr = StringUtils.split(str, "$");
				LBS lbs = new LBS();
				lbs.setMcc(arr[0]);
				lbs.setMnc(arr[1]);
				String bodyStr = StringUtils.substringAfter(lbsString, "|");
				String[] bodyArr = StringUtils.split(bodyStr, "|");
				lbs.setLac(Integer.parseInt(bodyArr[0], 16) + "");
				lbs.setCell(Integer.parseInt(bodyArr[1], 16) + "");
				lbs.setWatch(watch);
				watch.addLbs(lbs);
			}
			// watch.setLbsList(lbsList);
			// lbs.setWatch
			// savewwatch
			// if has longitude , parse gps
			if (!"0".equals(longitude) && !"0".equals(latitude)) {
				logger.info("QE -> GPS DATA ,lon:" + longitude + ",lat:"
						+ latitude);
				MapXY mapXY = new MapXY(longitude, latitude);
				BaiduAPI.transferToBDMap(mapXY);
				watch.setLongitude(mapXY.getX());
				watch.setLatitude(mapXY.getY());

			} else {
				// else parse lbs
				List<LBS> lbsList = watch.getLbsList();
				if (lbsList != null && lbsList.size() > 0) {
					LBS selctedLbs = lbsList.get(0);
					MiniGPSRequest req = new MiniGPSRequest();
					CellTower ct = new CellTower(selctedLbs.getMcc(),
							Integer.parseInt(selctedLbs.getMnc()) + "",
							selctedLbs.getLac(), selctedLbs.getCell());
					req.addCellTower(ct);
					try {
						MiniGPSResult rst = MiniGPS.getGPS(req);
						if (rst != null) {
							logger.info("MiniGPS Location:" + rst.getLocation());
							double lat = rst.getLocation().getLatitude();
							rst.getLocation().getAddress().getStreet();
							double lon = rst.getLocation().getLongitude();
							MapXY pointMapXY = new MapXY();
							pointMapXY.setX(lon + "");
							pointMapXY.setY(lat + "");
							// transferToBDMap(pointMapXY);
							watch.setStreet(rst.getLocation().getAddress()
									.getStreet());
							watch.setLatitude(pointMapXY.getY());
							watch.setLongitude(pointMapXY.getX());
						} else {
							logger.error("LBS Parse Failed.");
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			watchService.insertWatch(watch);
			CacheUtil.getWatchQueue().put(watch);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
