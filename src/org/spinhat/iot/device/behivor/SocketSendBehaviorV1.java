package org.spinhat.iot.device.behivor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.spinhat.iot.logger.IotSysdLogger;
import org.spinhat.iot.util.DeviceSession;

public class SocketSendBehaviorV1 implements SocketSendBehavior {
	
	private Socket deviceSocket;
	BufferedReader in = null;
	PrintWriter out = null;
	
	public SocketSendBehaviorV1(Socket deviceSocket) 
	{
		this.deviceSocket = deviceSocket;
		try 
		{
			in = new BufferedReader(new InputStreamReader(deviceSocket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(deviceSocket.getOutputStream()));
			IotSysdLogger.logger("info", "Initial device Socket I/O");
		}
		catch (Exception e)
		{
			IotSysdLogger.logger("fatal", "Initial device Socket I/O fail");
		}
	}
	
	// 如果 return null 就是有問題
	@Override
	public String send(String cmd) 
	{
		if (deviceSocket.isClosed() || ! deviceSocket.isConnected())
		{
			IotSysdLogger.logger("fatal", "Socket Closed or Socket cannot connected - return null");
			return null;
		}
		
		// TODO Auto-generated method stub
		String response = null;
		String sessionID = DeviceSession.get_session_id(4);
		
		try 
		{
			cmd = sessionID + ":" + cmd;
			
			out.write(cmd + "\n");
			// 發現當 對方 Ctrl+C 之後，照理說 in.readLine 應會會 hang 住，直到 timedout , 但是在這個程式裡面卻不會。
			// 這是因為我們一直有在 write 到 write queue 裡，當對方 socket 斷掉之後， write 的 data 會又被 in.readLine 讀回來 , 很詭異。
			out.flush();
			response = in.readLine();
		}
		catch (SocketTimeoutException ste)
		{
			ste.printStackTrace();
			IotSysdLogger.logger("fatal", "SocketTimeoutException " + ste.getMessage());
			response = null;
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			IotSysdLogger.logger("fatal", "IOException " + ioe.getMessage());
			response = null;
		}
		catch (Exception e)
		{
			IotSysdLogger.logger("fatal", "Exception" + e.getMessage());
			response = null;
		}

		// 當 device 自己斷掉 Ctrl+C 後，response 會等於 null
		if (response == null )
		//if (response == null || ! response.startsWith(sessionID + ":"))
		{
			IotSysdLogger.logger("fatal", "send response is null, close Socket I/O resources");
			this.close();
			return null;
		}
		return response.substring(sessionID.length() + 1);
	}
	
	private void close()
	{
		try 
		{
			this.in.close();
			this.out.close();
			this.deviceSocket.close();
		}
		catch (Exception e)
		{
			IotSysdLogger.logger("fatal", "Close device Socket I/O fail");
		}
	}
}
