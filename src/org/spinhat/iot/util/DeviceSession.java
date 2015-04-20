package org.spinhat.iot.util;

public class DeviceSession {
    public static String get_session_id(int length)  
    {
        StringBuffer buffer = new StringBuffer();
        String characters = "";

        characters = "abcdefghijklmnopqrstuvwxyz1234567890";
        
        int charactersLength = characters.length();

        for (int i = 0; i < length; i++) {
            double index = Math.random() * charactersLength;
            buffer.append(characters.charAt((int) index));
        }
        return buffer.toString();
    }
}
