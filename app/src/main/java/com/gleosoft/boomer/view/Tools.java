package com.gleosoft.boomer.view;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 */

public class Tools {

    /**
     * @return
     */
    public static String getServerData() {
        String acceptStr = null;
        try {
            InetAddress serverAddr = InetAddress.getByName(IAcceptServerData.SERVERIP);
            DatagramSocket socket = new DatagramSocket();
            String str = "000";
            byte[] buf = str.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, IAcceptServerData.SERVERPORT);
            socket.send(packet);

            byte[] buffer = new byte[buf.length];
            DatagramPacket recvPacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(recvPacket);
            InetAddress ad = recvPacket.getAddress();
            String s = ad.getHostAddress();
            acceptStr = new String(recvPacket.getData());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return acceptStr;

    }

}
