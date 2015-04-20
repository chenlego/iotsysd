package org.spinhat.iot.device.behivor;

import java.net.Socket;

final public class SocketSendHello extends SocketSendBehaviorV1 {
	public SocketSendHello(Socket socket)
	{
		super(socket);
	}
	
	public String sayHello()
	{
		return super.send("helo");
	}
}
