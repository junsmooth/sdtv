package com.potevio.sdtv.device.ythtjr.android;

public enum BedStatus {
	IN_BED("20", "在床"), OUT_BED("50", "离床"), BODY_MOVE("41", "体动"), NURSE_CALL(
			"30", "护士呼叫"), NURSE_CALL_TWICE("31", "护士呼叫两次"), WET_BODY_MOVE(
			"C1", "尿湿,体动"), WET_IN_BED("A0", "尿湿,在床"), WET_OUT_BED("D0",
			"尿湿,离床"), WET_NURSE_CALL("B0", "尿湿,护士呼叫"), WET_NURSE_CALL_TWICE(
			"B1", "尿湿,护士呼叫两次");
	private String code;
	private String name;

	private BedStatus(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static BedStatus getByCode(String code) {
		BedStatus[] statusArr = BedStatus.values();
		for (BedStatus status : statusArr) {
			if (status.getCode().equals(code)) {
				return status;
			}
		}
		return null;

	}

	public static String getNameByCode(String code) {
		BedStatus status = getByCode(code);
		if (status != null) {
			return status.getName();
		}
		return "";
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static boolean isStatusInBed(String statusCode) {
		if ("50".equals(statusCode) || "D0".equals(statusCode))
			return false;
		return true;
	}
}
