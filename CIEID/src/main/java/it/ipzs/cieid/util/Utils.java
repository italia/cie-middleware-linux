package it.ipzs.cieid.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Utils {

	public static Image scaleimage(int wid, int hei, BufferedImage img){
	    Image im = img;
	    double scale;
	    double imw = img.getWidth();
	    double imh = img.getHeight();
	    if (wid > imw && hei > imh){
	        im = img;
	    }else if(wid/imw < hei/imh){
	        scale = wid/imw;
	        im = img.getScaledInstance((int) (scale*imw), (int) (scale*imh), Image.SCALE_SMOOTH);
	    }else if (wid/imw > hei/imh){
	        scale = hei/imh;
	        im = img.getScaledInstance((int) (scale*imw), (int) (scale*imh), Image.SCALE_SMOOTH);
	    }else if (wid/imw == hei/imh){
	        scale = wid/imw;
	        im = img.getScaledInstance((int) (scale*imw), (int) (scale*imh), Image.SCALE_SMOOTH);
	    } 
	    return im;
	}
	
	public static String getAppDir()
	{
		String home = System.getProperty("user.home");
		String appDir = home + File.separator + ".CIEID";
		
		File appDirFile = new File(appDir);
		if(!appDirFile.exists())
			appDirFile.mkdirs();
		
		return appDir;
	}
	
	public static String getProperty(String key, String def)
	{
		String appDir = getAppDir();
		
		Properties props = new Properties();
		
		try {
			FileInputStream fins = new FileInputStream(new File(appDir, "cieid.props"));
			props.load(fins);
			fins.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return props.getProperty(key, def);
	}
	
	public static void setProperty(String key, String val)
	{
		String appDir = getAppDir();
		
		Properties props = new Properties();
		
		try {
			FileInputStream fins = new FileInputStream(new File(appDir, "cieid.props"));
			props.load(fins);
			fins.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		props.setProperty(key, val);
		
		try {
			FileOutputStream fouts = new FileOutputStream(new File(appDir, "cieid.props"), false);
			props.store(fouts, "CIEID Properties");
			fouts.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
