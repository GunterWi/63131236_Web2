package com.nqt.ListObject.models;

public class SinhVien {
	private String maSoSV;
	private String hoVaTen;
	
	public SinhVien(String maSoSV, String hoVaTen) {
		super();
		this.maSoSV = maSoSV;
		this.hoVaTen = hoVaTen;
	}
	public SinhVien() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getMaSoSV() {
		return maSoSV;
	}
	public void setMaSoSV(String maSoSV) {
		this.maSoSV = maSoSV;
	}
	public String getHoVaTen() {
		return hoVaTen;
	}
	public void setHoVaTen(String hoVaTen) {
		this.hoVaTen = hoVaTen;
	}
}