package org.spinhat.iot.device;

import java.io.IOException;
import java.net.Socket;

import org.spinhat.iot.IotServer;
import org.spinhat.iot.device.Device;
import org.spinhat.iot.device.behivor.ParseBehavior;
import org.spinhat.iot.device.behivor.ParseBehaviorV1;
import org.spinhat.iot.device.behivor.SocketSendHello;
import org.spinhat.iot.handler.DeviceHandler;
import org.spinhat.iot.logger.IotSysdLogger;
import org.spinhat.iot.util.DBOperator;
import org.spinhat.iot.util.RedisOperator;

import tw.com.andrewtech.device.AT_M0001;

public class Dispatcher extends Thread {
	private Socket deviceSocket;
	private RedisOperator redisOP = new RedisOperator();
	private IotServer iotServer = null;
	private ParseBehavior parserBehivor;
	
	public Dispatcher(IotServer iotServer, Socket deviceSocket)
	{
		this.iotServer = iotServer;
		this.deviceSocket = deviceSocket;
		this.parserBehivor = new ParseBehaviorV1();
	}
	
	public void run()
	{	
		SocketSendHello socketHello = new SocketSendHello(this.deviceSocket);
		String name = null;
		String model = null;
		String id = null;
		
		String response = socketHello.sayHello();
		if (response != null)
		{
			String data = this.parse(response);
			IotSysdLogger.logger("info", "Hello response : " + response);
			if (data != null)
			{
				String[] cols = data.split(":");
				model = cols[0];
				id    = cols[1];
				name  = model + "_" + id;
				if (this.isDeviceActivated(model, id) && ! isDeviceAlive(name))
				{
					redisOP.updateDeviceAliveStatus(name);
					Device device = this.dispatch(model, id);
					DeviceHandler dh = device.getHandler();
					iotServer.addHandler(dh);
					return;
				}
			}
			else
			{
				IotSysdLogger.logger("info", "Hello data parse fail");
			}
		}
		else
		{
			IotSysdLogger.logger("error", "Hello response is null");
		}
		this.rejectRequest();
	}
	
	private String parse(String response)
	{
		return this.parserBehivor.extract(response);
	}
		
	private Device dispatch(String model, String id)
	{
		switch(model)
		{
			case "AT-M0001":
				return new AT_M0001(model, id, this.deviceSocket);
		}
		return null;
	}
	
	private boolean isDeviceAlive(String name)
	{
		if ( redisOP.isDeviceAlive(name) )
		{
			IotSysdLogger.logger("info", name + " was handled , ignore this request");
			return true;
		}
		return false;
	}
	
	private boolean isDeviceActivated(String model, String machine_id)
	{
		// DBOperator dbop = new DBOperator();
		// return dbop.is_device_exist(model, machine_id);
		// from mysql
		DBOperator dbop = new DBOperator();
		if ( ! dbop.isDeviceActivated(model, machine_id) )
		{
			IotSysdLogger.logger("error", model + "_" + machine_id + " is not activated");
			return false; 
		}
		return true;
	}
	
	public void rejectRequest()
	{
		try {
			IotSysdLogger.logger("error", "Dispatcher reject client conneciton !");
			this.deviceSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			IotSysdLogger.logger("error", e.getMessage());
		}
	}
}
