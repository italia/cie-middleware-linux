package it.ipzs.carousel;

public class Cie {

	String name;
	String serialNumber;
	String pan;
	Boolean isCustomSign;
	
	public Cie(String pan, String name, String serialNumber)
	{
		this.name = name;
		this.serialNumber = serialNumber;
		this.pan = pan;
		this.isCustomSign = false;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getSerialNumber()
	{
		return this.serialNumber;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public Boolean getIsCustomSign() {
		return isCustomSign;
	}

	public void setIsCustomSign(Boolean isCustomSign) {
		this.isCustomSign = isCustomSign;
	}
	
}
