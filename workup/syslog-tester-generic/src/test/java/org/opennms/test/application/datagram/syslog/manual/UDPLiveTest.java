package org.opennms.test.application.datagram.syslog.manual;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opennms.test.application.datagram.syslog.SimpleLogSender;
import org.opennms.test.application.datagram.syslog.SimpleLogServer;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UDPLiveTest {
	private SimpleLogSender client;

	@Before
	public void setup() throws IOException {
		String host="localhost";
		int port=4445;
		
		new SimpleLogServer(port).start();
		
		client = new SimpleLogSender(host, port);
	}

	@Test
	public void sendMessageTest() {
		client.sendMessage("hello server");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@After
	public void tearDown() {
		stopEchoServer();
		client.close();
	}

	private void stopEchoServer() {
		client.sendMessage("SHUTDOWN");
	}

}