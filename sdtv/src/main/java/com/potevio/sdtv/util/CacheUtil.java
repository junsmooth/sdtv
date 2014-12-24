package com.potevio.sdtv.util;

import java.util.concurrent.LinkedBlockingQueue;

import com.potevio.sdtv.device.hiyo.HiyoMSG;
import com.potevio.sdtv.device.syshelp.WatchMSG;
import com.potevio.sdtv.device.ythtjr.BedMSG;

public class CacheUtil {

	private static LinkedBlockingQueue<BedMSG> ythtjrbedQueue = new LinkedBlockingQueue<BedMSG>();
	private static LinkedBlockingQueue<WatchMSG> syshelpWatchQueue = new LinkedBlockingQueue<WatchMSG>();
	
	private static LinkedBlockingQueue<HiyoMSG> hiyoQueue=new LinkedBlockingQueue<HiyoMSG>();
	public static LinkedBlockingQueue<HiyoMSG> getHiyoQueue() {
		return hiyoQueue;
	}

	private static BedMSG bedlatest = null;
	
	private static WatchMSG watchLatest=null;
	
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
