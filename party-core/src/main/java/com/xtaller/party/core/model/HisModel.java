package com.xtaller.party.core.model;

public class HisModel implements java.io.Serializable {
	private String name;
	private String xh;
	private String mz;
	private String pid;
	private String academic;
	private String myclass;
	private String sex;
	private Integer age;

	public HisModel() {
		this.setPid(pid);
		this.setXh(xh);
		this.setAcademic(academic);
		this.setName(name);
		this.setMz(mz);
		this.setMyclass(myclass);
		this.setSex(sex);
		this.setAge(age);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMz() {
		return mz;
	}

	public void setMz(String mz) {
		this.mz = mz;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getMyclass() {
		return myclass;
	}

	public void setMyclass(String myclass) {
		this.myclass = myclass;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getAcademic() {
		return academic;
	}

	public void setAcademic(String academic) {
		this.academic = academic;
	}

}
