package it.ipzs.carousel;

public class Cie {

	String name;
	String serialNumber;
	String pan;
	
	public Cie(String pan, String name, String serialNumber)
	{
		this.name = name;
		this.serialNumber = serialNumber;
		this.pan = pan;
		
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
	
	
}
