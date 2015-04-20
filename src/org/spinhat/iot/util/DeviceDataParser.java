package org.spinhat.iot.util;

import org.spinhat.iot.logger.IotSysdLogger;

public class DeviceDataParser {
	// col1               : cmd
	// col2               : length
	// col3               : converter
	// col4 ~ last_col -1 : data
	// last_col           : status code
	
	
	public static String parse(String input)
	{
		String[] cols = defineData(input);
		if (cols == null)
		{
			return null;
		}
				
		if (verifyData(cols))
		{
			switch(cols[2])
			{
				case "c1":
					return convert3B2N(cols[3]);
				default: // nc (no convert)
					return cols[3];
			}
		}
		else
		{
			return null;
		}
	}
	
	
	private static String convert3B2N(String input)
	{
		float value = 0.0f;
		String[] cols = input.split(":");
		String result = "";
		for (int i=0; i < cols.length; i++)
		{
			if (cols[i].length() == 3)
			{			
				value = Integer.valueOf(cols[i], 16);
				value = (float)((value - 1000) / 10);
			}
			
			result = result + String.valueOf(value);
			
			if (i != (cols.length -1))
			{
				result = result + ":";
			}
		}
		return result;
	}
	
	private static boolean verifyData(String[] cols)
	{
		if (isInteger(cols[1]))
		{
			int data_columns = Integer.parseInt(cols[1]);
			// 如果長度不 match , 且 return code 不是 2 開頭就表示有問題。
			if (cols[3].split(":").length == data_columns && cols[4].startsWith("2"))
			{
				return true;
			}
			IotSysdLogger.logger("error", "Data verify fail: return code != 2xx or column length mismatch");
		}
		return false;
	}
	
	private static String[] defineData(String input)
	{
		// col1               : cmd
		// col2               : length
		// col3               : converter
		// col4 ~ last_col -1 : data
		// last_col           : status code
		String[] cols = input.split(":");
		String[] results = new String[5];
		StringBuilder data = new StringBuilder(); 

		if (cols.length < 5)
		{
			IotSysdLogger.logger("error", "Data parse fail: data column is small than 5 - " + input);
			return null;
		}
		else
		{
			results[0] = cols[0];
			results[1] = cols[1];
			results[2] = cols[2];
			for (int i=3; i < (cols.length-1); i++)
			{
				data.append(cols[i]);
				if (i != (cols.length-2))
					data.append(":");
			}
			results[3] = data.toString(); 
			results[4] = cols[cols.length-1];
		}
		return results;
	}
	
	private static boolean isInteger(String str)
	{
		try 
		{
			Integer.parseInt(str);
			return true;
		}
		catch (NumberFormatException nfe)
		{
			IotSysdLogger.logger("error", "Convert String to Integer fail");
			return false;
		}
	}
}