package org.spinhat.iot;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.spinhat.iot.device.Dispatcher;
import org.spinhat.iot.handler.DeviceHandler;
import org.spinhat.iot.logger.IotSysdLogger;

public class IotServer extends Thread {
	private ServerSocket iotServerSocket;
	private int serverPort;
	private int maxClient;
	ThreadPoolExecutor pool = null; 
	
	public IotServer(int serverPort, int maxClient) 
	{		
		this.serverPort = serverPort;
		this.maxClient = maxClient;
		pool = new ThreadPoolExecutor(maxClient, maxClient, 10, TimeUnit.SECONDS, new java.util.concurrent.SynchronousQueue(), this.rejectedExecutionHandler);
	}
	
    private final RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			IotSysdLogger.logger("error", "Conncetions was exceed max client setting : " + maxClient );
            DeviceHandler handler = (DeviceHandler) r;
            handler.closeDevice();
        }
    };
	
	public void run() 
	{
		try 
		{
			IotSysdLogger.logger("info", "Starting the IoT Server at port:" + serverPort);
			this.iotServerSocket = new ServerSocket(this.serverPort); 
			
			while (IotSysd.isRunning) 
			{
				Socket deviceSocket = this.iotServerSocket.accept();
				Thread dispatcher = new Dispatcher(this, deviceSocket);
				dispatcher.start();
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			IotSysdLogger.logger("error", e.getMessage());
		}
	}
	
	public void addHandler(Thread dh)
	{
		this.pool.execute(dh);
	}
}
