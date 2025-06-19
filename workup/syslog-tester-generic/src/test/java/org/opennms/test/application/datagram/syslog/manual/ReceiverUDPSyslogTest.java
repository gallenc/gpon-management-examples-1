package org.opennms.test.application.datagram.syslog.manual;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opennms.test.application.datagram.syslog.SimpleLogSender;
import org.opennms.test.application.datagram.syslog.SimpleLogServer;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// simply starts receiver to show the logs received as datagrames
public class ReceiverUDPSyslogTest {
   private SimpleLogSender client;

   public static final int SYSLOG_SERVER_PORT = 4445;

   public static final int SYSLOG_OPENNMS_PORT = 10514;

   public static final boolean USE_SIMPLE_LOG_SERVER = true;

   @Before
   public void setup() throws IOException {
      String host = "localhost";

      int port;

      if (USE_SIMPLE_LOG_SERVER) {
         port = SYSLOG_SERVER_PORT;
         new SimpleLogServer(port).start();
      } else {
         port = SYSLOG_OPENNMS_PORT;
      }

      client = new SimpleLogSender(host, port);
   }

   @Test
   public void receiveMessageTest() {

      try {
         Thread.sleep(250000);
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   @After
   public void tearDown() {
      if (USE_SIMPLE_LOG_SERVER) {
         stopEchoServer();
      }

      client.close();
   }

   private void stopEchoServer() {
      client.sendMessage("SHUTDOWN");
   }

}