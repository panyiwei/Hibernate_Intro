package com.amaker.bean;

public class Pay {
	private int monthlyPay;//月薪
	private int yearPay;//年薪
	private int vocationWithPay;//年休假
	
	private Worker worker;//在此文件中写上整体类Worker后，可以在映射文件的组件component下写parent
	
	public int getMonthlyPay() {
		return monthlyPay;
	}
	public void setMonthlyPay(int monthlyPay) {
		this.monthlyPay = monthlyPay;
	}
	public int getYearPay() {
		return yearPay;
	}
	public void setYearPay(int yearPay) {
		this.yearPay = yearPay;
	}
	public int getVocationWithPay() {
		return vocationWithPay;
	}
	public void setVocationWithPay(int vocationWithPay) {
		this.vocationWithPay = vocationWithPay;
	}
	public Worker getWorker() {
		return worker;
	}
	public void setWorker(Worker worker) {
		this.worker = worker;
	}
}
