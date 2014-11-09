package com.bepol.beaconNActivities;

import android.app.Activity;
import android.os.Bundle;
/* Information class for show to user */
public class BeaconSignal{
	
	/* address 반환 */
	public String getAddress() {
		return address;
	}

	/* address 설정 */
	public void setAddress(String address) {
		this.address = address;
	}

	/* RSSI 반환 */
	public int getRssi() {
		return rssi;
	}

	/* RSSI 설정 */
	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	/* Major 반환 */
	public String getMajor() {
		return major;
	}

	/* Major 설정 */
	public void setMajor(String major) {
		this.major = major;
	}
	
	/* name 설정 */
	public void setName(String name){
		this.name = name;
	}
	
	/* name 반환 */
	public String getName(){
		return name;
	}

	/* Minor 반환 */
	public String getMinor() {
		return minor;
	}

	/* Minor 설정 */
	public void setMinor(String minor) {
		this.minor = minor;
	}

	private String address;
	private int rssi;
	private String major;
	private String minor;
	private String name;
	
	/* 생성자 */
	public BeaconSignal(String address, int rssi, String major, String minor){
		this.address = address;
		this.rssi = rssi;
		this.major = major;
		this.minor = minor;
	}
}
