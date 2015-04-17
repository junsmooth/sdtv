package com.potevio.sdtv.device.ythtjr.android.socket;

import org.apache.mina.core.session.IoSession;

public class AndroidMSG {
	public static final String HEARTBEAT = "HEARTBEAT";
	public static final String HEARTBEAT_RSP = "HEARTBEAT _RSP";
	public static final String AREA_LIST = "AREA_LIST";
	public static final String AREA_LIST_RSP = "AREA_LIST_RSP";

	public static final String BED_LIST = "BED_LIST";
	public static final String BED_DATA = "BED_DATA";
	public static final String BED_LIST_RSP = "BED_LIST_RSP";
	public static final String BED_STATUS_LIST = "BED_STATUS_LIST";
	public static final String BED_STATUS_LIST_RSP = "BED_STATUS_LIST_RSP";
	public static final String HEART_BREATH_LIST = "HEART_BREATH_LIST";
	public static final String HEART_BREATH_LIST_RSP = "HEART_BREATH_LIST_RSP";
	public static final String AREA_REPORT_RSP = "AREA_REPORT_RSP";
	public static final String AREA_REPORT = "AREA_REPORT";
	public static final String PAT_LIST = "PAT_LIST";
	public static final String PAT_LIST_RSP = "PAT_LIST_RSP";
	public static final String PAT_DETAIL = "PAT_DETAIL";
	public static final String PAT_DETAIL_RSP = "PAT_DETAIL_RSP";
	public static final String WATCH_LIST_RSP = "WATCH_LIST_RSP";
	protected static final String ALARM_WM = "ALARM_WM";
	public static final String ALARM_WM_OP_RSP = "ALARM_WM_OP_RSP";

	private String type;
	private Object data;
	private IoSession session;

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	@Override
	public String toString() {
		return "BedAndroidMSG [type=" + type + ", data=" + data + ", session="
				+ session + "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
