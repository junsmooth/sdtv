package com.potevio.sdtv.util;

import java.util.concurrent.LinkedBlockingQueue;

import com.potevio.sdtv.device.syshelp.WatchMSG;
import com.potevio.sdtv.device.ythtjr.BedMSG;

public class CacheUtil {

	private static LinkedBlockingQueue<BedMSG> ythtjrbedQueue = new LinkedBlockingQueue<BedMSG>();
	private static LinkedBlockingQueue<WatchMSG> syshelpWatchQueue = new LinkedBlockingQueue<WatchMSG>();

	public static LinkedBlockingQueue<WatchMSG> getSyshelpWatchQueue() {
		return syshelpWatchQueue;
	}

	public static LinkedBlockingQueue<BedMSG> getYthtjrbedQueue() {
		return ythtjrbedQueue;
	}

}
