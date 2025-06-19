package org.opennms.test.application.datagram.nokia.pri.syslog.manual;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opennms.test.application.datagram.syslog.NokiaEventLogFull;
import org.opennms.test.application.datagram.syslog.SimpleLogSender;
import org.opennms.test.application.datagram.syslog.SimpleLogServer;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SendNokiaPriSyslogTranslatorOpenNMSTest {
   private SimpleLogSender client;
   
   public boolean USE_SYSLOG_PRI=true;

   public static final int SYSLOG_SERVER_PORT = 4445;

   public static final int SYSLOG_OPENNMS_PORT = 10514;

   public static final boolean USE_SIMPLE_LOG_SERVER = false;

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
   public void sendMessageTest() {

      String logEntry = "Feb 28 16:35:13 LT1-blk1-olt-301 - APP_NAME:alarm_logic_app,APP_VERSION:2212.640,MODULE_NAME:alarm,ENTITY_NAME:ALCLFCA45C31,ENTITY_TYPE:rssi-onu,alarm-type-id:onu-upstream-rx-power-exceed-threshold,event-time:2024-02-28T16:35:13+00:00,perceived-severity:minor,alarm-text:low-alarm < onu-upstream-rx-power(-31.5 dBm) < low-warning, OLT xFP operational limits exceeded";

      NokiaEventLogFull eventParser = new NokiaEventLogFull();

      boolean match = eventParser.parseLogEntry(logEntry);
      assertTrue(match);

      System.out.println("Event Parser values parsed from log: " + eventParser.toString());

      // test that the class outputs the same log as parsed
      String receivedLogEntry = eventParser.toLogEntry(USE_SYSLOG_PRI);

      System.out.println("Event parser toLogEntry: " + receivedLogEntry);

      client.sendMessage(receivedLogEntry);

      try {
         Thread.sleep(5000);
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