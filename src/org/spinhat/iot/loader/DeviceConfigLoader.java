package org.spinhat.iot.loader;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import org.spinhat.iot.IotSysdConfig;
import org.spinhat.iot.device.DeviceCMD;
import org.spinhat.iot.logger.IotSysdLogger;

public class DeviceConfigLoader {
	
	private Properties prop = new Properties();
	
	public DeviceConfigLoader(String deviceName)
	{
		String deviceConfPath = IotSysdConfig.getDeviceConfPath(); 
		try 
		{
			String config = deviceConfPath + "/" + deviceName + ".properties";
			prop.load(new FileInputStream(config));
		}
		catch (Exception e) 
		{
			IotSysdLogger.logger("fata", e.getMessage());
			e.printStackTrace();
		}	
	}
	
	public ArrayList<DeviceCMD> getCMDs()
	{
		ArrayList<DeviceCMD> cmdList = new ArrayList<DeviceCMD>();
		Enumeration<?> req = prop.propertyNames(); 
	    while (req.hasMoreElements()) 
	    {
	        Object obj = (Object) req.nextElement();
	        String key = obj.toString();
	        String pattern = "cmd\\d+";
	        if ( key.matches(pattern) )
	        {
	        	String cmd = prop.getProperty(key);
	        	String[] cols = cmd.split(":");
	        	cmdList.add(new DeviceCMD(cols[0], cols[1], Integer.parseInt(cols[2])));
	        }
	    }
		return cmdList;
	}
	
	public int getTimedOut()
	{
		return Integer.parseInt(prop.getProperty("timedout"));
	}
}
