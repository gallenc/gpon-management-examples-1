package org.opennms.test.application.datagram.calix.pri.syslog.file.manual;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class ParseEventsFilePriTest {
   

   public static final boolean SEND_EVENT_TO_OPENNMS = true;

   public static final boolean USE_SYSLOG_PRI = false;

   public static final int SYSLOG_OPENNMS_PORT = 10514;

   public static final boolean CHANGE_LOG_TIME_TO_TODAY = true;

   public static final boolean LOG_TIME_FORMAT_CHANGE = true;
   
   public static final boolean REVERSE_ORDER=true; 
   
   public static final boolean GENERATE_TIMESTAMP=true;
   
   public static final String SOURCE_LOG_FILE="./src/test/resources/syslogevents/cov745_ONT_Raw_events_Cluster2.csv";


   private boolean sendEventToOpenNMS = SEND_EVENT_TO_OPENNMS;

   private boolean useSyslogPri = USE_SYSLOG_PRI;

   private int syslogOpenNMSPort = SYSLOG_OPENNMS_PORT;

   private boolean changeLogTimeToToday = CHANGE_LOG_TIME_TO_TODAY;

   private boolean logTimeFormatChange = LOG_TIME_FORMAT_CHANGE;
   
   private boolean reverseOrder=REVERSE_ORDER; 
   
   private boolean generateTimestamp=GENERATE_TIMESTAMP;

   private String sourceLogFile =SOURCE_LOG_FILE ;
   
   private String adjustPerceivedSeverity=null; // if null do not adjust

   private SimpleLogSender client;
   
   public void adjustPerceivedSeverity(String perceivedSeverity) {
      this.adjustPerceivedSeverity=perceivedSeverity;
   }

   public void setSendEventToOpenNMS(boolean sendEventToOpenNMS) {
      this.sendEventToOpenNMS = sendEventToOpenNMS;
   }

   public void setUseSyslogPri(boolean useSyslogPri) {
      this.useSyslogPri = useSyslogPri;
   }

   public void setSyslogOpenNMSPort(int syslogOpenNMSPort) {
      this.syslogOpenNMSPort = syslogOpenNMSPort;
   }

   public void setChangeLogTimeToToday(boolean changeLogTimeToToday) {
      this.changeLogTimeToToday = changeLogTimeToToday;
   }

   public void setLogTimeFormatChange(boolean logTimeFormatChange) {
      this.logTimeFormatChange = logTimeFormatChange;
   }

   public void setReverseOrder(boolean reverseOrder) {
      this.reverseOrder = reverseOrder;
   }

   public void setGenerateTimestamp(boolean generateTimestamp) {
      this.generateTimestamp = generateTimestamp;
   }

   public void setSourceLogFile(String sourceLogFile) {
      this.sourceLogFile = sourceLogFile;
   }

   @Before
   public void setup() throws IOException {
      String host = "localhost";

      int port;

      if (sendEventToOpenNMS) {
         port = syslogOpenNMSPort;
         client = new SimpleLogSender(host, port);
      }

   }

   @After
   public void tearDown() {
      if (client != null)
         client.close();
   }

   @Test
   public void test1() {
      System.out.println("sending events from:"+sourceLogFile);
      Scanner scanner = null;

      PrintWriter hostsFileWriter = null;
      PrintWriter lteMappingFileWriter = null;
      PrintWriter sentFileFileWriter=null;

      SortedMap<String, String> hostMap = new TreeMap<>();

      // lteSerialNo, lteHostname
      SortedMap<String, List<String>> oltLteMapping = new TreeMap<>();

      IncrimentingIpAddress ipAddress = new IncrimentingIpAddress("172.20.0.150");
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
         
         // file for output logs
         File sentFile = new File("./target/sentSyslogs.txt");
         sentFile.delete();
         sentFile.createNewFile();
         sentFileFileWriter = new PrintWriter(sentFile);

         scanner = new Scanner(new File(sourceLogFile));

         int logCount = 0;
         int calexLogSuccess = 0;
         int otherLogFullSuccess = 0;
         int otherLogPartialSuccess = 0;
         int failedParse = 0;
         
         List<String> logLines = new ArrayList<String>();
         
         if(reverseOrder) {
            List<String> logLinesReversed = new ArrayList<String>();
            while (scanner.hasNextLine()) {
               logLinesReversed.add(scanner.nextLine());
            }
            for(int idx=logLinesReversed.size()-1 ; idx>=0 ; idx--) {
               logLines.add(logLinesReversed.get(idx));
            }
            
         } else {
            while (scanner.hasNextLine()) {
               logLines.add(scanner.nextLine());
            }
         }
         
         // used to generate timestamps relative to before now
         SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
         int timeBetweenEvents = 1000; // 1000 ms 1s
         long startTime = new Date().getTime() - timeBetweenEvents * logLines.size();
         long delta = 0;

         Iterator<String> logLinesIterator = logLines.iterator();
         while (logLinesIterator.hasNext()) {
            String logEntry = logLinesIterator.next();

            if (logTimeFormatChange) {

               try {
                  System.out.println("logEntry date:" + logEntry);

                  // pattern for 2024-07-15T03:11:14+00:00
                  SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

                  int tsend = logEntry.indexOf(" ");
                  String dateStr = logEntry.substring(0, tsend);

                  Date t = inputDateFormat.parse(dateStr);

                  //pattern for Feb 28 00:36:00 lec191-olt-1
                  SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM dd hh:mm:ss");
                  outputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                  String ds = outputDateFormat.format(t);
                  System.out.println("changed date:" + ds);

                  logEntry = ds + logEntry.substring(tsend);

                  System.out.println("output logEntry:" + logEntry);

               } catch (ParseException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
            }

            logCount++;
            String nodeName = null;

            // try calexEventLog parser
            CalexAxosEventLog calexAxosEventLog = new CalexAxosEventLog();
            boolean parsed = calexAxosEventLog.parseLogEntry(logEntry);

            if (parsed) {
               
               if (generateTimestamp) {
                  Date date = new Date(startTime + delta);
                  calexAxosEventLog.setTimestampStr(df.format(date));
                  delta = delta + timeBetweenEvents;
               }
               
               if(adjustPerceivedSeverity!=null) {
                  calexAxosEventLog.adjustPerceivedSeverity(adjustPerceivedSeverity);
               }
               
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

               String parsedLog = calexAxosEventLog.toLogEntry(useSyslogPri);
               //
               boolean match = generateTimestamp || logEntry.equals(parsedLog);
               if (!match) {
                  System.out.println("CalexAxosEventLog no match:");
                  System.out.println("   " + logEntry);
                  System.out.println("   " + parsedLog);
               } else {
                  calexLogSuccess++;
                  if (sendEventToOpenNMS) {
                     if (changeLogTimeToToday) {
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

                     String receivedLogEntry = calexAxosEventLog.toLogEntry(useSyslogPri);
                     //System.out.println("sending log: " + receivedLogEntry);
                     sentFileFileWriter.println(receivedLogEntry);
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
                  
                  if (generateTimestamp) {
                     Date date = new Date(startTime + delta);
                     nokiaEventLogFull.setTimestampStr(df.format(date));
                     delta = delta + timeBetweenEvents;
                  }
                  
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

                  String parsedLog = nokiaEventLogFull.toLogEntry(useSyslogPri);
                  //
                  boolean match = generateTimestamp || logEntry.equals(parsedLog);
                  if (!match) {
                     System.out.println("OtherEventLogFull no match:");
                     System.out.println("   " + logEntry);
                     System.out.println("   " + parsedLog);
                  } else {
                     otherLogFullSuccess++;
                     if (changeLogTimeToToday) {
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
                     if (sendEventToOpenNMS) {
                        String receivedLogEntry = nokiaEventLogFull.toLogEntry(useSyslogPri);
                        //System.out.println("sending log: " + receivedLogEntry);
                        sentFileFileWriter.println(receivedLogEntry);
                        client.sendMessage(receivedLogEntry);
                     }
                  }
               } else {
                  // try OtherEventLogPartial parser
                  NokiaEventLogPartial nokiaEventLogPartial = new NokiaEventLogPartial();
                  parsed = nokiaEventLogPartial.parseLogEntry(logEntry);
                  if (parsed) {
                     
                     if (generateTimestamp) {
                        Date date = new Date(startTime + delta);
                        nokiaEventLogPartial.setTimestampStr(df.format(date));
                        delta = delta + timeBetweenEvents;
                     }

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

                     String parsedLog = nokiaEventLogPartial.toLogEntry(useSyslogPri);
                     //
                     boolean match = generateTimestamp || logEntry.equals(parsedLog);
                     if (!match) {
                        System.out.println("OtherEventLogPartial no match:");
                        System.out.println("   " + logEntry);
                        System.out.println("   " + parsedLog);
                     } else {
                        otherLogPartialSuccess++;
                        if (changeLogTimeToToday) {
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
                        if (sendEventToOpenNMS) {
                           String receivedLogEntry = nokiaEventLogPartial.toLogEntry(useSyslogPri); // no PRI
                           //System.out.println("sending log: " + receivedLogEntry);
                           
                           sentFileFileWriter.println(receivedLogEntry);
                           
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

            if (nodeName == null) {
               System.out.println("no nodeName in logEntry:\n" + logEntry);
            } else {
               System.out.println("null ip address for " + nodeName + " in logEntry:\n" + logEntry);
               hostMap.put(nodeName, null);
            }

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
         if (sentFileFileWriter != null)
            sentFileFileWriter.close();
         
      }
   }
}
