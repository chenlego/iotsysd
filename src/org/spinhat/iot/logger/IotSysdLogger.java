package org.spinhat.iot.logger;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.spinhat.iot.IotSysdConfig;

public class IotSysdLogger {
	
	static {
		PropertyConfigurator.configure(IotSysdConfig.getLogConfig());
	}

	public static void logger(String level, String message)
	{
		String className = "";
		int debug = IotSysdConfig.getDebugMode();
		Logger logger = Logger.getLogger("IoTSystemLogger");		

		if (debug == 1)
		{
			className =  new Throwable().getStackTrace()[1].getClassName(); 
			message = className + ": " + message;
		}
		
		switch(level)
		{
			case "info":
				logger.info(message);
				break;
			case "debug":
				logger.debug(message);
				break;
			case "warn":
				logger.warn(message);
				break;
			case "error":
				logger.error(message);
				break;
			case "fatal":
				logger.fatal(message);
				break;
			default :
				logger.debug(message);
				break;
		}
	}
}
