package com.potevio.sdtv.device.minigps;

import java.util.ArrayList;
import java.util.List;

public class MiniGPSRequest {

	private String version = "1.1.0";
	private String host = "maps.google.com";
	private List<CellTower> cell_towers;
	private String verifycode;
	public String getVersion() {
		return version;
	}
	public void addCellTower(CellTower tower){
		if(cell_towers==null){
			cell_towers=new ArrayList<CellTower>();
		}
		cell_towers.add(tower);
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public List<CellTower> getCell_towers() {
		return cell_towers;
	}
	public void setCell_towers(List<CellTower> cell_towers) {
		this.cell_towers = cell_towers;
	}
	public String getVerifycode() {
		return verifycode;
	}
	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}

}
