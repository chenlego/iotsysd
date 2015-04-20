package org.spinhat.iot.device;

import java.util.ArrayList;

public class DeviceCMD {
	private int duration;
	private String cmd;
	private String type;
	
	public DeviceCMD(String cmd, String type, int duration)
	{ 
		this.cmd = cmd;
		this.type = type;
		this.duration = duration;
	}
	
	public String getCmd()
	{
		return this.cmd;
	}

	public int getDuration()
	{
		return this.duration;
	}
	
	public boolean isWriteCMD()
	{
		if (this.type.equals("write"))
			return true;
		else
			return false;
	}
	
	private static int gcd(int m, int n) 
	{
	    return n == 0 ? m : gcd(n, m % n);
	}
	    
	private static int lcm(int m, int n) 
	{ 
	 	return m * n / gcd(m, n);
	}

	public static int getTotalDuration(ArrayList<DeviceCMD> cmd_list)
	{
		int max = 1;
		for (DeviceCMD deviceCmd : cmd_list)
		{
			if (deviceCmd.getDuration() == 0)
			{
				continue;
			}
			max = lcm(max, deviceCmd.getDuration());
		}
		return max;
	}
}
