package org.spinhat.iot.handler;

import java.util.ArrayList;

import org.spinhat.iot.device.Device;
import org.spinhat.iot.device.DeviceCMD;
import org.spinhat.iot.util.DBOperator;
import org.spinhat.iot.util.RedisOperator;

public abstract class DeviceHandler extends Thread {
	protected int default_timedout = 8000;
	protected String name = null;
	protected String model = null;
	protected String id = null;
	RedisOperator redisOP = new RedisOperator();
	DBOperator dbOP = new DBOperator();
	protected Device device = null;
	protected ArrayList<DeviceCMD> cmdList = null ;
	
	public DeviceHandler(Device device)
	{
		this.device = device;
		this.cmdList = this.device.getCommands();
		this.name = device.getName();
	}
	
	abstract public void run();
	
	public void closeDevice()
	{
		this.device.close();
	}

	// redis
	protected String getWriteDataByCMD(String name, String cmd)
	{
		return this.redisOP.getWriteDataByCMD(name, cmd);
	}

	// redis
	protected void updateWriteStatusByCMD(String name, String cmd, String status)
	{
		this.redisOP.updateWriteStatusByCMD(name, cmd, status);
	}
	
	// redis
	protected void updateDeviceData(String name, String cmd, String data)
	{
		this.redisOP.updateDeviceData(name, cmd, data);
	}
	
	// redis
	protected void clearDeviceAliveStatus(String name)
	{
		this.redisOP.clearAliveStatus(name);
	}
	
	// redis
	protected void delWriteDataByCMD(String name, String cmd)
	{
		this.redisOP.delWriteDataByCMD(name, cmd);
	}
}
