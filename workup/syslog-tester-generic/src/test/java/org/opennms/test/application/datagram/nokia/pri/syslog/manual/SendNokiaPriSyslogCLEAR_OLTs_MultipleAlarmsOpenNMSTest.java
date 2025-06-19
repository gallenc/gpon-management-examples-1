package org.opennms.test.application.datagram.nokia.pri.syslog.manual;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opennms.test.application.datagram.syslog.NokiaEventLogFull;

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

public class SendNokiaPriSyslogCLEAR_OLTs_MultipleAlarmsOpenNMSTest {
   private SimpleLogSender client;

   public static final int SYSLOG_SERVER_PORT = 4445;

   public static final int SYSLOG_OPENNMS_PORT = 10514;

   public static final boolean USE_SIMPLE_LOG_SERVER = false;

   public static final boolean CHANGE_LOG_TIME_TO_TODAY = true;

   public static final boolean GENERATE_TIMESTAMP = true;

   public static final boolean USE_SYSLOG_PRI = true;
   
   // if has value will replace all in example alarms
   public static final String perceivedSeverity = "cleared";

   // olts tied to LT1-she504-olt-702
   public static final String mainOLT =  "LT1-she504-olt-702";
            
  // olts tied to LT1-she504-olt-702_SECONDARY  LT1-she504-olt-702
  // olt 600935 = serial number ALCLFCA3FB0C
   public static final List<String> ONT_SERIAL_NUMBERS = Arrays.asList("ALCLFCA3FB0C");


   // OLT CRITICAL RAISE MESSAGE TYPES NOKIA

   //Mar 12 09:00:45 LT1-wel7-olt-101 - APP_NAME:alarm_logic_app,APP_VERSION:2212.640,MODULE_NAME:alarm,ENTITY_NAME:637720/ENET_UNI_10GE,ENTITY_TYPE:onu/interface,alarm-type-id:lan-los,event-time:2024-03-12T09:00:45+00:00,perceived-severity:major,alarm-text:LAN-LOS (No carrier at the Ethernet UNI)

   public static final String LAN_LOS = "Mar 12 09:00:45 LT1-wel7-olt-101 - APP_NAME:alarm_logic_app,APP_VERSION:2212.640,MODULE_NAME:alarm,ENTITY_NAME:637720/ENET_UNI_10GE,ENTITY_TYPE:onu/interface,alarm-type-id:lan-los,event-time:2024-03-12T09:00:45+00:00,perceived-severity:major,alarm-text:LAN-LOS (No carrier at the Ethernet UNI)";

   //Mar 12 08:44:18 LT1-roc1-olt-301 - APP_NAME:alarm_logic_app,APP_VERSION:2212.640,MODULE_NAME:alarm,ENTITY_NAME:604219/ANI,ENTITY_TYPE:onu/interface,alarm-type-id:transceiver-link-rx-power,event-time:2024-03-12T08:44:18+00:00,perceived-severity:major,alarm-text:Low received optical power (Received downstream optical power below threshold)

   public static final String TRANSCEIVER_LINK_RX_POWER = "Mar 12 08:44:18 LT1-roc1-olt-301 - APP_NAME:alarm_logic_app,APP_VERSION:2212.640,MODULE_NAME:alarm,ENTITY_NAME:604219/ANI,ENTITY_TYPE:onu/interface,alarm-type-id:transceiver-link-rx-power,event-time:2024-03-12T08:44:18+00:00,perceived-severity:major,alarm-text:Low received optical power (Received downstream optical power below threshold)";

   public static final List<String> CAUSE_LOGS = Arrays.asList(LAN_LOS, TRANSCEIVER_LINK_RX_POWER);

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



      SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
      int timeBetweenEvents = 1000; // 1000 ms 1s
      long startTime = new Date().getTime() - timeBetweenEvents * ONT_SERIAL_NUMBERS.size() * CAUSE_LOGS.size();

      long delta = 0;
      
      // send events for different ONT IDs
      for (String ontSerialNumber : ONT_SERIAL_NUMBERS) {

         // send events for different cause names
         for (String causeLog : CAUSE_LOGS) {

            NokiaEventLogFull eventParser = new NokiaEventLogFull();

            // check we are parsing this value
            boolean match = eventParser.parseLogEntry(causeLog);
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

            if (GENERATE_TIMESTAMP) {
               Date date = new Date(startTime + delta);
               eventParser.setTimestampStr(df.format(date));
               delta = delta + timeBetweenEvents;
            }

            //eventParser.setSerialNumber(ontSerialNumber);
            
            if(perceivedSeverity!=null) {
               eventParser.setPerceivedSeverity(perceivedSeverity);
            }
            
            eventParser.setNodename(mainOLT);

            String receivedLogEntry = eventParser.toLogEntry(USE_SYSLOG_PRI);

            System.out.println("Sending ont-id=" + ontSerialNumber + " Event parser toLogEntry: " + receivedLogEntry);

            client.sendMessage(receivedLogEntry);

         }

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