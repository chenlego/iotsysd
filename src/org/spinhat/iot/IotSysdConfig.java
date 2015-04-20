package org.spinhat.iot;

import java.io.FileInputStream;
import java.util.Properties;

import org.spinhat.iot.logger.IotSysdLogger;

public class IotSysdConfig {

	private static Properties prop = new Properties();
	
	static 
	{
		try 
		{
			prop.load(new FileInputStream("/etc/iotsysd/iotsysd.properties"));
		}
		catch (Exception e) 
		{
			IotSysdLogger.logger("fata", e.getMessage());
			e.printStackTrace();
		}
	}
	
	// Server Config
	public static int getIoTServerPort()
	{
		return Integer.parseInt(prop.getProperty("serverPort", "9134"));
	}
	public static int getIoTServerMaxClient()
	{
		return Integer.parseInt(prop.getProperty("maxClient", "32"));
	}
	
	// Log Config
	public static String getLogConfig()
	{
		return prop.getProperty("logConfig");
	}
	
	// Device Config
	public static String getDeviceConfPath()
	{
		return prop.getProperty("deviceConfPath");
	}
	
	// System Config
	public static int getDebugMode()
	{
		return Integer.parseInt(prop.getProperty("debugMode", "0"));
	}

	// Redis Config
	public static String getRedisMaster()
	{
		return prop.getProperty("redisMaster");
	}
	
	public static int getRedisPort()
	{
		return Integer.parseInt(prop.getProperty("redisPort", "6379"));
	}
	
	// MySQL Config
	public static String getMysqlMaster()
	{
		return prop.getProperty("mysqlMaster");
	}
	
	public static int getmysqlPort()
	{
		return Integer.parseInt(prop.getProperty("mysqlPort", "3306"));
	}
	
}
