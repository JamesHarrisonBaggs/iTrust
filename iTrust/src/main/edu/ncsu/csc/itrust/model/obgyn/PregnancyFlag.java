package edu.ncsu.csc.itrust.model.obgyn;

public class PregnancyFlag {
	
	private boolean flag;
	private String name;
	private String description;
	
	public PregnancyFlag(boolean flag, String name, String description) {
		this.flag = flag;
		this.name = name;
		this.description = description;
	}
	
	public PregnancyFlag(boolean flag, String name) {
		this.flag = flag;
		this.name = name;
		this.description = "";
	}
	
	public boolean isFlag() {
		return flag;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}		
}

