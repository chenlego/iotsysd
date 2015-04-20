package org.spinhat.iot.device;

import java.net.Socket;
import java.util.ArrayList;

import org.spinhat.iot.device.behivor.ParseBehavior;
import org.spinhat.iot.device.behivor.SocketSendBehavior;
import org.spinhat.iot.handler.DeviceHandler;
import org.spinhat.iot.logger.IotSysdLogger;

public abstract class Device {
	protected String name;
	protected String model;
	protected String id;
	protected Socket deviceSocket;
	protected SocketSendBehavior sendBehavior;
	protected ParseBehavior parserBehivor;
	protected DeviceHandler handler;
	
	public Device(String model, String id, Socket socket)
	{
		this.name = model + "_" + id;
		this.model = model;
		this.id = id;
		this.deviceSocket = socket;
	}
	
	public abstract DeviceHandler getHandler();
	
	abstract public void getConfig(String config);
	
	abstract public String parseData(String data);
	
	abstract public boolean verifyData(String data);
	
	abstract public ArrayList<DeviceCMD> getCommands();
	
	abstract public String send(String cmd);
		
	public void close()
	{
		try 
		{
			this.deviceSocket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected void setTimeout(int sec)
	{
		try
		{
			this.deviceSocket.setSoTimeout(sec);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			IotSysdLogger.logger("error", e.getMessage());
		}
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getModel()
	{
		return this.name;
	}
	
	public String getID()
	{
		return this.name;
	}

}
