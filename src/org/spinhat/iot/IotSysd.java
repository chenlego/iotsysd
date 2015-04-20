package org.spinhat.iot;

import org.spinhat.iot.logger.IotSysdLogger;

public class IotSysd {
	
	public static boolean isRunning = true;
	
	public static void main(String[] args) throws InterruptedException {
		
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				IotSysdLogger.logger("info", "Service iotsysd Stop ... ");
				IotSysd.isRunning = false;
			}
		});
		
		// TODO Auto-generated method stub
		IotSysdLogger.logger("info", "Service iotsysd Start ... ");

		int serverPort = IotSysdConfig.getIoTServerPort();
		int maxClient  = IotSysdConfig.getIoTServerMaxClient();
		IotServer iot_server = new IotServer(serverPort, maxClient);
		iot_server.start();
	}
}
