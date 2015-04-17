package com.potevio.sdtv.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import com.potevio.sdtv.device.hiyo.HiyoMSG;
import com.potevio.sdtv.device.syshelp.WatchMSG;
import com.potevio.sdtv.device.ythtjr.BedMSG;
import com.potevio.sdtv.device.ythtjr.android.BedData;
import com.potevio.sdtv.domain.Watch;

public class CacheUtil {

	private static LinkedBlockingQueue<BedData> androidBedQueue = new LinkedBlockingQueue<BedData>();
	private static LinkedBlockingQueue<BedMSG> ythtjrbedQueue = new LinkedBlockingQueue<BedMSG>();
	private static LinkedBlockingQueue<WatchMSG> syshelpWatchQueue = new LinkedBlockingQueue<WatchMSG>();

	private static Map<String, List<BedData>> bedCacheMap = new HashMap<String, List<BedData>>();

	public static Map<String, List<BedData>> getBedCacheMap() {
		return bedCacheMap;
	}

	public static void setBedCacheMap(Map<String, List<BedData>> bedCacheMap) {
		CacheUtil.bedCacheMap = bedCacheMap;
	}

	private static LinkedBlockingQueue<Watch> watchQueue = new LinkedBlockingQueue<Watch>();

	public static LinkedBlockingQueue<Watch> getWatchQueue() {
		return watchQueue;
	}

	public static LinkedBlockingQueue<BedData> getAndroidBedQueue() {
		return androidBedQueue;
	}

	public static void setAndroidBedQueue(
			LinkedBlockingQueue<BedData> androidBedQueue) {
		CacheUtil.androidBedQueue = androidBedQueue;
	}

	public static void setWatchQueue(LinkedBlockingQueue<Watch> watchQueue) {
		CacheUtil.watchQueue = watchQueue;
	}

	private static LinkedBlockingQueue<HiyoMSG> hiyoQueue = new LinkedBlockingQueue<HiyoMSG>();

	private static LinkedBlockingQueue<String> syshelpMessageQueue = new LinkedBlockingQueue<String>();

	public static LinkedBlockingQueue<String> getSyshelpMessageQueue() {
		return syshelpMessageQueue;
	}

	public static LinkedBlockingQueue<HiyoMSG> getHiyoQueue() {
		return hiyoQueue;
	}

	private static BedMSG bedlatest = null;

	private static WatchMSG watchLatest = null;

	public static WatchMSG getWatchLatest() {
		return watchLatest;
	}

	public static void setWatchLatest(WatchMSG watchLatest) {
		CacheUtil.watchLatest = watchLatest;
	}

	public static BedMSG getBedlatest() {
		return bedlatest;
	}

	public static void setBedlatest(BedMSG bedlatest) {
		CacheUtil.bedlatest = bedlatest;
	}

	public static LinkedBlockingQueue<WatchMSG> getSyshelpWatchQueue() {
		return syshelpWatchQueue;
	}

	public static LinkedBlockingQueue<BedMSG> getYthtjrbedQueue() {
		return ythtjrbedQueue;
	}

}
