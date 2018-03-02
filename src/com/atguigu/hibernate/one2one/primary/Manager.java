package com.atguigu.hibernate.one2one.primary;

public class Manager {
	private Integer mgrId;
	private String mgrName;
	
	private Department depat;

	public Integer getMgrId() {
		return mgrId;
	}

	public void setMgrId(Integer mgrId) {
		this.mgrId = mgrId;
	}

	public String getMgrName() {
		return mgrName;
	}

	public void setMgrName(String mgrName) {
		this.mgrName = mgrName;
	}

	public Department getDepat() {
		return depat;
	}

	public void setDepat(Department depat) {
		this.depat = depat;
	}
}
