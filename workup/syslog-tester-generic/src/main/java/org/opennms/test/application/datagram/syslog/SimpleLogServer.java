package org.opennms.test.application.datagram.syslog;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SimpleLogServer extends Thread {
	
	public static final int SYSLOG_BUFFER_SIZE = 1024;

    protected DatagramSocket socket = null;
    protected boolean running;
    protected byte[] buf = new byte[SYSLOG_BUFFER_SIZE];

    public SimpleLogServer(int port ) throws IOException {
        socket = new DatagramSocket(port);
    }

    public void run() {
        running = true;

        while (running) {
            try {
            	System.out.println("waiting for messages");

                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("received: address: "+address
                		+ " port: "+port
                		+ " message: "+message);
                
                // shutsdown log server
                if (message.equals("SHUTDOWN")) {
                	System.out.println("shutting down server");
                    running = false;
                    continue;
                }
 
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            }
        }
        socket.close();
    }
}