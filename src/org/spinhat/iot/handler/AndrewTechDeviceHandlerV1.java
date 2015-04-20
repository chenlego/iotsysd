package org.spinhat.iot.handler;

import org.spinhat.iot.IotSysd;
import org.spinhat.iot.device.Device;
import org.spinhat.iot.device.DeviceCMD;
import org.spinhat.iot.logger.IotSysdLogger;

public class AndrewTechDeviceHandlerV1 extends DeviceHandler {

	public AndrewTechDeviceHandlerV1(Device device) {
		super(device);
	}

	@Override
	public void run() {
		IotSysdLogger.logger("info", "Device: " + device.getName() + " Joined !");
		// TODO Auto-generated method stub
		int count = 0;
		int modular = DeviceCMD.getTotalDuration(cmdList);
		boolean finished = false;
		String cmd = null;
		
		while(true)
		{
			try 
			{
				count++;
				if (finished || ! IotSysd.isRunning )
				{
					IotSysdLogger.logger("info", super.name + " Connection Closed !");
					super.closeDevice();
					super.clearDeviceAliveStatus(super.name);
					break;
				}

				for (DeviceCMD deviceCmd : cmdList)	
				{
					cmd = deviceCmd.getCmd();
					if (deviceCmd.isWriteCMD()) // write cmd 
					{
						if (! this.doWrite(cmd))
							finished = true;
					}
					else // read cmd
					{
						if ((count % deviceCmd.getDuration()) == 0)
						{
							if (! this.doRead(cmd))
								finished = true;
						}
					}
				}
				if (count == modular)
				{
					count = 0;
				}
				Thread.sleep(1000);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				IotSysdLogger.logger("error", e.getMessage());
				finished = true;
			}
		}
	}
	
	private boolean doWrite(String cmd)
	{
		String response = null;
		String cmdData = super.getWriteDataByCMD(this.name, cmd);
		if (cmdData == null)
		{
			return true;
		}
		
		IotSysdLogger.logger("info", super.name + " - send  => " + cmd + ":" + cmdData);
		response = device.send(cmd + ":" + cmdData);
		if (response != null)
		{
			IotSysdLogger.logger("fatal", super.name + " - recv  => " + response);
			String data = device.parseData(response);
			if (data != null)
			{
				super.updateWriteStatusByCMD(this.name, cmd, "OK");
				IotSysdLogger.logger("info", super.name + " - parse => " + cmd + ":" + data);
			}
			else
			{
				super.updateWriteStatusByCMD(this.name, cmd, "BAD");
				IotSysdLogger.logger("error", super.name + " - parse => " + cmd + ":" + "null");
			}
			super.delWriteDataByCMD(super.name, cmd);
			return true;
		}
		else
		{
			IotSysdLogger.logger("fatal", super.name + " - recv  => " + cmd + ":" + "response is null");
			return false;
		}
		
	}
	
	private boolean doRead(String cmd)
	{
		IotSysdLogger.logger("info", super.name + " - send  => " + cmd);
		String response = device.send(cmd);
		if (response != null)
		{
			IotSysdLogger.logger("info", super.name + " - recv  => " + response);

			String data = device.parseData(response);
			if (data != null)
			{
				IotSysdLogger.logger("info", super.name + " - parse => " + cmd + ":" + data);
				super.updateDeviceData(this.name, cmd, data);
			}
			else
			{
				IotSysdLogger.logger("error", super.name + " - parse => " + cmd + ":" + "null");
			}
			return true;
		}
		else
		{
			IotSysdLogger.logger("fatal", super.name + " - recv  => " + cmd + ":" + "response is null"); 
			return false;
		}
	}
}
