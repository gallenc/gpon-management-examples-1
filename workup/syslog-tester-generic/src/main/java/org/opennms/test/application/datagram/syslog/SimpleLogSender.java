package org.opennms.test.application.datagram.syslog;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SimpleLogSender {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;
    private int port;
       

    public SimpleLogSender(String host, int port) {
        try {
        	this.port=port;
            socket = new DatagramSocket();
            address = InetAddress.getByName(host);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String msg) {
        DatagramPacket packet = null;
        try {
            buf = msg.getBytes();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.println("sending packet "+packet+" message "+message);
            
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        socket.close();
    }
}
