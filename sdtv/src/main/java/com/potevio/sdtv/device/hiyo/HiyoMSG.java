package com.potevio.sdtv.device.hiyo;

import java.util.Arrays;
import java.util.Date;

public class HiyoMSG {
	
	private byte[] ver=new byte[2];
	private byte[] flag=new byte[2];
	private byte[] resv=new byte[2];
	private short len;
	private byte[] cmd=new byte[2];
	private byte[] cfalg=new byte[2];
	private byte[] reserved=new byte[2];
	
	private Date dataTime;
	public Date getDataTime() {
		return dataTime;
	}


	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}


	public HiyoMSG() {
		ver[0]=0x1;
		ver[1]=0x0;
		flag[0]=0x0;
		flag[1]=0x0;
		resv[0]=0x0;
		resv[1]=0x0;
		cfalg[0]=0x0;
		cfalg[1]=0x0;
		reserved[0]=0x0;
		reserved[1]=0x0;
	
	}
	
	
	@Override
	public String toString() {
		return "HiyoMSG [ver=" + Arrays.toString(ver) + ", flag="
				+ Arrays.toString(flag) + ", resv=" + Arrays.toString(resv)
				+ ", len=" + len + ", cmd=" + Arrays.toString(cmd) + ", cfalg="
				+ Arrays.toString(cfalg) + ", reserved="
				+ Arrays.toString(reserved) + ", ret=" + Arrays.toString(ret)
				+ ", msg=" + msg + ", crc=" + Arrays.toString(crc) + "]";
	}
	private byte[] ret=new byte[2];
	
	private String msg;
	private byte[] crc=new byte[4];
	public byte[] getVer() {
		return ver;
	}
	public void setVer(byte[] ver) {
		this.ver = ver;
	}
	public byte[] getFlag() {
		return flag;
	}
	public void setFlag(byte[] flag) {
		this.flag = flag;
	}
	public byte[] getResv() {
		return resv;
	}
	public void setResv(byte[] resv) {
		this.resv = resv;
	}
	public short getLen() {
		return len;
	}
	public void setLen(short len) {
		this.len = len;
	}
	public byte[] getCmd() {
		return cmd;
	}
	public void setCmd(byte[] cmd) {
		this.cmd = cmd;
	}
	public byte[] getCfalg() {
		return cfalg;
	}
	public void setCfalg(byte[] cfalg) {
		this.cfalg = cfalg;
	}
	public byte[] getReserved() {
		return reserved;
	}
	public void setReserved(byte[] reserved) {
		this.reserved = reserved;
	}
	public byte[] getRet() {
		return ret;
	}
	public void setRet(byte[] ret) {
		this.ret = ret;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public byte[] getCrc() {
		return crc;
	}
	public void setCrc(byte[] crc) {
		this.crc = crc;
	}
	
	
	
	
	
	
	
}
