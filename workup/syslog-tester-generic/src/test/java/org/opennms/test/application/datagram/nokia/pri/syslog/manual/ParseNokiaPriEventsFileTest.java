package org.opennms.test.application.datagram.nokia.pri.syslog.manual;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opennms.test.application.datagram.syslog.CalexAxosEventLog;
import org.opennms.test.application.datagram.syslog.NokiaEventLogFull;
import org.opennms.test.application.datagram.syslog.NokiaEventLogPartial;
import org.opennms.test.application.datagram.syslog.SimpleLogSender;
import org.opennms.test.application.datagram.syslog.manual.IncrimentingIpAddress;

public class ParseNokiaPriEventsFileTest {

   public static final boolean SEND_EVENT_TO_OPENNMS = true;

   public static final boolean USE_SYSLOG_PRI = true;

   public static final int SYSLOG_OPENNMS_PORT = 10514;

   public static final boolean CHANGE_LOG_TIME_TO_TODAY = true;

   private SimpleLogSender client;

   @Before
   public void setup() throws IOException {
      String host = "localhost";

      int port;

      if (SEND_EVENT_TO_OPENNMS) {
         port = SYSLOG_OPENNMS_PORT;
         client = new SimpleLogSender(host, port);
      }

   }

   @After
   public void tearDown() {
      if (client != null)
         client.close();
   }

   @Test
   public void test() {
      Scanner scanner = null;

      PrintWriter hostsFileWriter = null;
      PrintWriter lteMappingFileWriter = null;

      SortedMap<String, String> hostMap = new TreeMap<>();

      // lteSerialNo, lteHostname
      SortedMap<String, List<String>> oltLteMapping = new TreeMap<>();

      // STARTING HIGHER THAN OTHER LOGS
      IncrimentingIpAddress ipAddress = new IncrimentingIpAddress("172.20.2.100");
      try {
         // file for discovered hostnames
         File hostNamesFile = new File("./target/hostnames.txt");
         hostNamesFile.delete();
         hostNamesFile.createNewFile();
         hostsFileWriter = new PrintWriter(hostNamesFile);

         // file for discovered lte mappings
         File lteMappingFile = new File("./target/lteMappings.txt");
         lteMappingFile.delete();
         lteMappingFile.createNewFile();
         lteMappingFileWriter = new PrintWriter(lteMappingFile);

         scanner = new Scanner(new File("./src/test/resources/syslogevents/sampleLogsExtra-2-5-24_Nokia.txt"));

         int logCount = 0;
         int calexLogSuccess = 0;
         int otherLogFullSuccess = 0;
         int otherLogPartialSuccess = 0;
         int failedParse = 0;

         while (scanner.hasNextLine()) {
            String logEntry = scanner.nextLine();
            logCount++;
            String nodeName = null;

            // try calexEventLog parser
            CalexAxosEventLog calexAxosEventLog = new CalexAxosEventLog();
            boolean parsed = calexAxosEventLog.parseLogEntry(logEntry);

            if (parsed) {
               nodeName = calexAxosEventLog.getNodename();

               // get mapping between lte and ont from serial number
               List<String> values = new ArrayList<>();
               values.add(nodeName);

               String details = calexAxosEventLog.getDetails();
               String serialNoPattern = "(?s)SerialNo=(.*?)(,|$)";
               Pattern serialNoRegex = Pattern.compile(serialNoPattern);
               Matcher serialNoMatcher = serialNoRegex.matcher(details);
               String serialNo = "";
               if (serialNoMatcher.find()) {
                  serialNo = serialNoMatcher.group(1);
               }
               values.add(serialNo);

               // ont-id='378104']/
               String xpath = calexAxosEventLog.getXpath();
               String xPathPattern = "(?s)ont-id='(.*?)']";
               Pattern xPathRegex = Pattern.compile(xPathPattern);
               Matcher xPathMatcher = xPathRegex.matcher(xpath);
               String ontId = null;
               if (xPathMatcher.find()) {
                  ontId = xPathMatcher.group(1);
                  oltLteMapping.put(ontId, values);
               }

               String parsedLog = calexAxosEventLog.toLogEntry(USE_SYSLOG_PRI);
               //
               boolean match = logEntry.equals(parsedLog);
               if (!match) {
                  System.out.println("CalexAxosEventLog no match:");
                  System.out.println("   " + logEntry);
                  System.out.println("   " + parsedLog);
               } else {
                  calexLogSuccess++;
                  if (SEND_EVENT_TO_OPENNMS) {
                     if (CHANGE_LOG_TIME_TO_TODAY) {
                        // change the date time to be in same time as local time
                        System.out.println("Parsed date time (from example log) : " + calexAxosEventLog.getDay() + " " + calexAxosEventLog.getMonth() + " "
                                 + calexAxosEventLog.getTimestampStr());

                        LocalDate today = LocalDate.now();
                        Month month = today.getMonth();
                        String mon = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                        int day = today.getDayOfMonth();

                        calexAxosEventLog.setMonth(mon);
                        calexAxosEventLog.setDay(Integer.toString(day));

                        System.out.println("Log with revised date time : " + calexAxosEventLog.getDay() + " " + calexAxosEventLog.getMonth() + " "
                                 + calexAxosEventLog.getTimestampStr());
                     }

                     String receivedLogEntry = calexAxosEventLog.toLogEntry(USE_SYSLOG_PRI);
                     //System.out.println("sending log: " + receivedLogEntry);
                     client.sendMessage(receivedLogEntry);
                  }

                  //                  System.out.println("CalexAxosEventLog match:");
                  //                  System.out.println("   "+logEntry);
               }

            } else {
               // try NokiaEventLogFull parser
               NokiaEventLogFull nokiaEventLogFull = new NokiaEventLogFull();
               parsed = nokiaEventLogFull.parseLogEntry(logEntry);
               if (parsed) {
                  nodeName = nokiaEventLogFull.getNodename();

                  List<String> values = new ArrayList<>();
                  values.add(nodeName);

                  // ENTITY_NAME:600935  (same as oltid)
                  String entityName = nokiaEventLogFull.getEntityName();
                  values.add(entityName);

                  // get mapping between lte and ont from serial number
                  String alarmText = nokiaEventLogFull.getAlarmText();
                  String serialNoPattern = "(?s)Serial-Number=(.*?)(,|$)";
                  Pattern serialNoRegex = Pattern.compile(serialNoPattern);
                  Matcher serialNoMatcher = serialNoRegex.matcher(alarmText);
                  String serialNo = "";
                  if (serialNoMatcher.find()) {
                     serialNo = serialNoMatcher.group(1);
                  }
                  values.add(serialNo);
                  oltLteMapping.put(entityName, values);

                  String parsedLog = nokiaEventLogFull.toLogEntry(USE_SYSLOG_PRI);
                  //
                  boolean match = logEntry.equals(parsedLog);
                  if (!match) {
                     System.out.println("OtherEventLogFull no match:");
                     System.out.println("   " + logEntry);
                     System.out.println("   " + parsedLog);
                  } else {
                     otherLogFullSuccess++;
                     if (CHANGE_LOG_TIME_TO_TODAY) {
                        // change the date time to be in same time as local time
                        System.out.println("Parsed date time (from example log) : " + nokiaEventLogFull.getDay() + " " + nokiaEventLogFull.getMonth() + " "
                                 + nokiaEventLogFull.getTimestampStr());

                        LocalDate today = LocalDate.now();
                        Month month = today.getMonth();
                        String mon = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                        int day = today.getDayOfMonth();

                        nokiaEventLogFull.setMonth(mon);
                        nokiaEventLogFull.setDay(Integer.toString(day));

                        System.out.println("Log with revised date time : " + nokiaEventLogFull.getDay() + " " + nokiaEventLogFull.getMonth() + " "
                                 + nokiaEventLogFull.getTimestampStr());
                     }

                     //                     System.out.println("NokiaEventLog match:");
                     //                     System.out.println("   "+logEntry);
                     if (SEND_EVENT_TO_OPENNMS) {
                        String receivedLogEntry = nokiaEventLogFull.toLogEntry(USE_SYSLOG_PRI);
                        //System.out.println("sending log: " + receivedLogEntry);
                        client.sendMessage(receivedLogEntry);
                     }
                  }
               } else {
                  // try OtherEventLogPartial parser
                  NokiaEventLogPartial nokiaEventLogPartial = new NokiaEventLogPartial();
                  parsed = nokiaEventLogPartial.parseLogEntry(logEntry);
                  if (parsed) {

                     nodeName = nokiaEventLogPartial.getNodename();

                     // List<String> values = new ArrayList<>();
                     // values.add(nodeName);

                     // no entity name in nokiaEventLogPartial
                     //  String entityName = null;
                     //  values.add(entityName);

                     // get mapping between lte and ont from serial number
                     // String alarmText = nokiaEventLogPartial.getAlarmText();
                     // String serialNoPattern = "(?s)Serial-Number=(.*?)(,|$)";
                     // Pattern serialNoRegex = Pattern.compile(serialNoPattern);
                     // Matcher serialNoMatcher = serialNoRegex.matcher(alarmText);
                     // if (serialNoMatcher.find()) {
                     //    String serialNo = serialNoMatcher.group(1);
                     //    oltLteMapping.put(serialNo, values);
                     // }

                     String parsedLog = nokiaEventLogPartial.toLogEntry(USE_SYSLOG_PRI);
                     //
                     boolean match = logEntry.equals(parsedLog);
                     if (!match) {
                        System.out.println("OtherEventLogPartial no match:");
                        System.out.println("   " + logEntry);
                        System.out.println("   " + parsedLog);
                     } else {
                        otherLogPartialSuccess++;
                        if (CHANGE_LOG_TIME_TO_TODAY) {
                           // change the date time to be in same time as local time
                           System.out.println("Parsed date time (from example log) : " + nokiaEventLogPartial.getDay() + " " + nokiaEventLogPartial.getMonth()
                                    + " " + nokiaEventLogPartial.getTimestampStr());

                           LocalDate today = LocalDate.now();
                           Month month = today.getMonth();
                           String mon = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                           int day = today.getDayOfMonth();

                           nokiaEventLogPartial.setMonth(mon);
                           nokiaEventLogPartial.setDay(Integer.toString(day));

                           System.out.println("Log with revised date time : " + nokiaEventLogPartial.getDay() + " " + nokiaEventLogPartial.getMonth() + " "
                                    + nokiaEventLogPartial.getTimestampStr());
                        }
                        //                        System.out.println("OtherEventLogPartial match:");
                        //                        System.out.println("   "+logEntry);
                        if (SEND_EVENT_TO_OPENNMS) {
                           String receivedLogEntry = nokiaEventLogPartial.toLogEntry(USE_SYSLOG_PRI); // no PRI
                           //System.out.println("sending log: " + receivedLogEntry);
                           client.sendMessage(receivedLogEntry);
                        }
                     }
                  } else {
                     failedParse++;
                     System.out.println("OtherEventLogPartial not parsed: ");
                     System.out.println("   " + logEntry);
                  }
               }
            }

            if(nodeName==null) {
               System.out.println("nodeName is null ");
            } else hostMap.put(nodeName, null);

         }

         scanner.close();
         System.out.println("file test finished"
                  + "\n  logCount=" + logCount
                  + "\n  calexLogSuccess=" + calexLogSuccess
                  + "\n  otherLogFullSuccess=" + otherLogFullSuccess
                  + "\n  otherLogPartialSuccess=" + otherLogPartialSuccess
                  + "\n  failedParse=" + failedParse
                  + "\n  unique hosts=" + hostMap.size());

         // print out unique nodenames
         for (String nodeNameKey : hostMap.keySet()) {
            hostsFileWriter.println(ipAddress.toString() + " " + nodeNameKey);
            ipAddress = ipAddress.next();
         }

         // print out lte ont mapping
         for (String lteSerialNo : oltLteMapping.keySet()) {
            String values = lteSerialNo;
            for (String value : oltLteMapping.get(lteSerialNo)) {
               values = values + "," + value;
            }
            lteMappingFileWriter.println(values);
         }

      } catch (

      FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         if (scanner != null)
            scanner.close();
         if (hostsFileWriter != null)
            hostsFileWriter.close();
         if (lteMappingFileWriter != null)
            lteMappingFileWriter.close();
      }
   }
}
