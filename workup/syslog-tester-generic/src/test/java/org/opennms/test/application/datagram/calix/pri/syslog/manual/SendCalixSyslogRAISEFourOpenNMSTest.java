package org.opennms.test.application.datagram.calix.pri.syslog.manual;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opennms.test.application.datagram.syslog.CalexAxosEventLog;
import org.opennms.test.application.datagram.syslog.SimpleLogSender;
import org.opennms.test.application.datagram.syslog.SimpleLogServer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;

import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SendCalixSyslogRAISEFourOpenNMSTest {
   private SimpleLogSender client;

   public static final int SYSLOG_SERVER_PORT = 4445;

   public static final int SYSLOG_OPENNMS_PORT = 10514;

   public static final boolean USE_SIMPLE_LOG_SERVER = false;

   public static final boolean CHANGE_LOG_TIME_TO_TODAY = true;
   
   public static final boolean GENERATE_TIMESTAMP = true;

   public static final boolean USE_SYSLOG_PRI = true;
   
   // olts tied to lec191-olt-1_SECONDARY
   List<String> ontids = Arrays.asList("61180","124010","130749","397513");
   //List<String> ontids = Arrays.asList("61180","124010","130749");
   //List<String> ontids = Arrays.asList("61180","124010");

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
      
    //String logEntry = "Feb 28 00:36:00 lec191-olt-1 notfmgrd[6203]: [1][1][A][6203] [23] Id:5030, Syslog-Severity:3, Perceived-Severity:MINOR, Name:high-laser-bias, Category:PON Cause:High laser bias., Details:SerialNo=E7D3FA, Xpath:/config/system/ont[ont-id='61180'] Address:NULL, Primary-element:NULL, Value:NULL, Verb:NULL, Session:0, Login:NULL, IpAddress:NULL, SrcManager:NULL, Secondary-element:NULL";

      String logEntry = "Mar 12 10:29:25 lec191-olt-1 notfmgrd[6212]: [1][1][A][6212] [23] Id:5031, Syslog-Severity:3, Perceived-Severity:MINOR, Name:low-rx-opt-pwr-fe, Category:PON Cause:The ONT reports low received optical power from the OLT., Details:SerialNo=AE4BB3, Xpath:/config/system/ont[ont-id='130749'] Address:NULL, Primary-element:NULL, Value:NULL, Verb:NULL, Session:0, Login:NULL, IpAddress:NULL, SrcManager:NULL, Secondary-element:NULL";

      CalexAxosEventLog eventParser = new CalexAxosEventLog();

      boolean match = eventParser.parseLogEntry(logEntry);
      assertTrue(match);

      System.out.println("Event Parser values parsed from log: " + eventParser.toString());

      if (CHANGE_LOG_TIME_TO_TODAY) {
         // change the date time to be in same time as local time
         System.out.println("Parsed date time (from example log) : " + eventParser.getDay() + " " + eventParser.getMonth() + " " + eventParser.getTimestampStr());

         LocalDate today = LocalDate.now();
         Month month = today.getMonth();
         String mon = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
         int day = today.getDayOfMonth();

         eventParser.setMonth(mon);
         eventParser.setDay(Integer.toString(day));

         System.out.println("Log with revised date time : " + eventParser.getDay() + " " + eventParser.getMonth() + " " + eventParser.getTimestampStr());
      }
      
      // send events for different ONT IDs

      SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
      int timeBetweenEvents = 1000; // 1000 ms 1s
      long startTime = new Date().getTime() - timeBetweenEvents * ontids.size();
      
      long delta=0;
      for (String ontid: ontids) {
         if (GENERATE_TIMESTAMP) {
            Date date = new Date(startTime+delta);
            eventParser.setTimestampStr(df.format(date));
            delta = delta+timeBetweenEvents;
         }
         
         eventParser.setXpath("/config/system/ont[ont-id='"+ontid + "']");
         String receivedLogEntry = eventParser.toLogEntry(USE_SYSLOG_PRI);

         System.out.println("Sending ont-id="+ontid+ " Event parser toLogEntry: " + receivedLogEntry);

         client.sendMessage(receivedLogEntry);
         
      }

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