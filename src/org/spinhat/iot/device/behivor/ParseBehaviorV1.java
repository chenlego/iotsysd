package org.spinhat.iot.device.behivor;

import org.spinhat.iot.util.DeviceDataParser;

public class ParseBehaviorV1 implements ParseBehavior {

	@Override
	public String extract(String input) {
		// TODO Auto-generated method stub
		return DeviceDataParser.parse(input);
	}

	@Override
	public boolean verify() {
		// TODO Auto-generated method stub
		return false;
	}

}
