package com.potevio.sdtv.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SocketUtil {
	private static Logger logger = LoggerFactory.getLogger(SocketUtil.class);
	public static int byteArrayToInt(byte[] b, int offset, int len) {
		int value = 0;
		for (int i = 0; i < len; i++) {
			int shift = (len - 1 - i) * 8;
			value += (b[i + offset] & 0x000000FF) << shift;
		}
		return value;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		StringBuffer strBuf = new StringBuffer("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;

			String hv = Integer.toHexString(v);
			strBuf.append(hv + " ");
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
}
