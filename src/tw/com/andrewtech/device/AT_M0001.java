package tw.com.andrewtech.device;

import java.net.Socket;
import java.util.ArrayList;

import org.spinhat.iot.device.Device;
import org.spinhat.iot.device.DeviceCMD;
import org.spinhat.iot.device.behivor.ParseBehaviorV1;
import org.spinhat.iot.device.behivor.SocketSendBehaviorV1;
import org.spinhat.iot.handler.AndrewTechDeviceHandlerV1;
import org.spinhat.iot.handler.DeviceHandler;
import org.spinhat.iot.loader.DeviceConfigLoader;

public class AT_M0001 extends Device {

	private static ArrayList<DeviceCMD> cmdList = new ArrayList<DeviceCMD>();
	private static final int timedout;
	
	static 
	{
		DeviceConfigLoader loader = new DeviceConfigLoader("AT_M0001");
		cmdList = loader.getCMDs();
		timedout = loader.getTimedOut();
	}
	
	public AT_M0001(String model, String id, Socket socket) {
		super(model, id, socket);	
		// TODO Auto-generated constructor stub
		sendBehavior = new SocketSendBehaviorV1(socket);
		parserBehivor = new ParseBehaviorV1();
		handler = new AndrewTechDeviceHandlerV1(this);
		super.setTimeout(AT_M0001.timedout);
	}
	
	@Override
	public DeviceHandler getHandler() {
		// TODO Auto-generated method stub
		return handler;
	}

	@Override
	public void getConfig(String config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String parseData(String data) {
		// TODO Auto-generated method stub
		return parserBehivor.extract(data);
	}

	@Override
	public boolean verifyData(String data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<DeviceCMD> getCommands() {
		// TODO Auto-generated method stub
		return AT_M0001.cmdList;
	}

	@Override
	public String send(String cmd) 
	{
		// TODO Auto-generated method stub
		return sendBehavior.send(cmd);
	}
}
