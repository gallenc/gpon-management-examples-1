package org.opennms.test.application.datagram.syslog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalexAxosEventLog {
   private String rawMatch = null;
   private String pri = null;
   private String priValue = null;
   private String month = null;
   private String day = null;
   private String timestampStr = null;
   private String nodename = null;
   private String shelfId = null;
   private String slotId = null;
   private String activeOrStandby = null;
   private String processId = null;
   private String logFacility = null;
   private String event = null;
   private String id = null;
   private String syslogSeverity = null;
   private String perceivedSeverity = null;
   private String logName = null;
   private String logCategory = null;
   private String logCause = null;
   private String details = null;
   private String xpath = null;
   private String address = null;
   private String additionalInfo = null;

   public String getRawMatch() {
      return rawMatch;
   }

   public String getPri() {
      return pri;
   }

   public void setPri(String pri) {
      this.pri = pri;
   }

   public String getPriValue() {
      return priValue;
   }

   public void setPriValue(String priValue) {
      this.priValue = priValue;
   }

   public String getMonth() {
      return month;
   }

   public void setMonth(String month) {
      this.month = month;
   }

   public String getDay() {
      return day;
   }

   public void setDay(String day) {
      this.day = day;
   }

   public String getTimestampStr() {
      return timestampStr;
   }

   public void setTimestampStr(String timestampStr) {
      this.timestampStr = timestampStr;
   }

   public String getNodename() {
      return nodename;
   }

   public void setNodename(String nodename) {
      this.nodename = nodename;
   }

   public String getShelfId() {
      return shelfId;
   }

   public void setShelfId(String shelfId) {
      this.shelfId = shelfId;
   }

   public String getSlotId() {
      return slotId;
   }

   public void setSlotId(String slotId) {
      this.slotId = slotId;
   }

   public String getActiveOrStandby() {
      return activeOrStandby;
   }

   public void setActiveOrStandby(String activeOrStandby) {
      this.activeOrStandby = activeOrStandby;
   }

   public String getProcessId() {
      return processId;
   }

   public void setProcessId(String processId) {
      this.processId = processId;
   }

   public String getLogFacility() {
      return logFacility;
   }

   public void setLogFacility(String logFacility) {
      this.logFacility = logFacility;
   }

   public String getEvent() {
      return event;
   }

   public void setEvent(String event) {
      this.event = event;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getSyslogSeverity() {
      return syslogSeverity;
   }

   public void setSyslogSeverity(String syslogSeverity) {
      this.syslogSeverity = syslogSeverity;
   }

   public String getPerceivedSeverity() {
      return perceivedSeverity;
   }

   public void setPerceivedSeverity(String perceivedSeverity) {
      this.perceivedSeverity = perceivedSeverity;
   }

   public String getLogName() {
      return logName;
   }

   public void setLogName(String logName) {
      this.logName = logName;
   }

   public String getLogCategory() {
      return logCategory;
   }

   public void setLogCategory(String logCategory) {
      this.logCategory = logCategory;
   }

   public String getLogCause() {
      return logCause;
   }

   public void setLogCause(String logCause) {
      this.logCause = logCause;
   }

   public String getDetails() {
      return details;
   }

   public void setDetails(String details) {
      this.details = details;
   }

   public String getXpath() {
      return xpath;
   }

   public void setXpath(String xpath) {
      this.xpath = xpath;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getAdditionalInfo() {
      return additionalInfo;
   }

   public void setAdditionalInfo(String additionalInfo) {
      this.additionalInfo = additionalInfo;
   }

   /**
    * Mapping percievedSeverity alarm levels to syslog levels - used for <PRI> calculation
    * Syslog log-level AXOS alarm severity level
    *  0 (Emergency)
    *  1 (Alert)
    *  2 (Critical)    Critical
    *  3 (Error)       Major, Minor
    *  4 (Warning)     Warning
    *  5 (Notice)      
    *  6 (Info)        Info, Clear
    *  7 (Debug)       None
    *  NOTE DIFFERENCE    *  
    *  https://datatracker.ietf.org/doc/rfc5674/
    *  ITU Perceived Severity      syslog SEVERITY (Name)
    *      Critical                    1 (Alert)
    *      Major                       2 (Critical)
    *      Minor                       3 (Error)
    *      Warning                     4 (Warning)
    *      Indeterminate               5 (Notice)
    *      Cleared                     5 (Notice)
    */
   SyslogSeverity mapCalexSeverity(String percievedSeverity) {
      switch (percievedSeverity.toLowerCase()) {
      case "critical":
         return SyslogSeverity.CRITICAL;
      case "major":
      case "minor":
         return SyslogSeverity.ERROR;
      case "warning":
         return SyslogSeverity.WARNING;
      case "info":
      case "clear":
         return SyslogSeverity.INFORMATIONAL;
      case "none":
         return SyslogSeverity.DEBUG;
      default:
         return SyslogSeverity.INFORMATIONAL;
      }
   }

   /**
    * this sets perceived severity and also pri
    * @param percievedSeverity
    */
   public void adjustPerceivedSeverity(String percievedSeverity) {
      SyslogSeverity syslogSeverity = mapCalexSeverity(percievedSeverity);
      this.setPerceivedSeverity(percievedSeverity);
      this.setSyslogSeverity(Integer.toString(syslogSeverity.numericalCode()));
      priValue = calculatePri();
   }

   
   public String calculatePri() {
      String priValue = null;
      try {
         int severity = Integer.parseInt(syslogSeverity);
         int facility = Integer.parseInt(logFacility);
         int priValueNo = (facility * 8) + severity;
         priValue = Integer.toString(priValueNo);
      } catch (Exception ex) {
         priValue = "000"; // if unable to create a pri
      }
      return priValue;
   }

   /**
    * parse a log entry from axos r21 logs
    * see https://www.calix.com/content/dam/calix/mycalix-misc/lib/iae/axos/21x/mmtg/index.htm?toc.htm?95527.htm
    * @param logEntry
    * @return
    */
   public boolean parseLogEntry(String logEntry) {

      //https://www.calix.com/content/dam/calix/mycalix-misc/lib/iae/axos/21x/mmtg/index.htm?toc9782322.htm?106614.htm

      // ((?s)(<(.*?)>|)(\D{3})\s(\d|\d{2})\s(.{8})\s(.*?)(?=\snotfmgrd)\s(?:.*?)\s\[(.*?)\]\[(.*?)\]\[(.*?)\]\[(.*?)\]\s\[(.*?)\]\s(.*?)Id:(.*?),\sSyslog-Severity:([0-9]+),\sPerceived-Severity:(.*?),\sName:(.*?),\sCategory:(.*?)\sCause:(.*?),\sDetails:(.*?),\sXpath:(.*?)\sAddress:(.*?),\s(.*)

      String pattern = "(?s)(<(.*?)>|)(\\D{3})\\s(\\d|\\d{2})\\s(.{8})\\s(.*?)(?=\\snotfmgrd)\\s(?:.*?)\\s\\[(.*?)\\]\\[(.*?)\\]\\[(.*?)\\]\\[(.*?)\\]\\s\\[(.*?)\\]\\s(.*?)Id:(.*?),\\sSyslog-Severity:([0-9]+),\\sPerceived-Severity:(.*?),\\sName:(.*?),\\sCategory:(.*?)\\sCause:(.*?),\\sDetails:(.*?),\\sXpath:(.*?)\\sAddress:(.*?),\\s(.*)";

      // Create a Pattern object
      Pattern r = Pattern.compile(pattern);

      // Now create matcher object.
      Matcher m = r.matcher(logEntry);

      if (m.find()) {
         rawMatch = m.group(0);
         pri = m.group(1);
         priValue = m.group(2);
         month = m.group(3);
         day = m.group(4);
         timestampStr = m.group(5);
         nodename = m.group(6);
         shelfId = m.group(7);
         slotId = m.group(8);
         activeOrStandby = m.group(9);
         processId = m.group(10);
         logFacility = m.group(11);
         event = m.group(12);
         id = m.group(13);
         syslogSeverity = m.group(14);
         perceivedSeverity = m.group(15);
         logName = m.group(16);
         logCategory = m.group(17);
         logCause = m.group(18);
         details = m.group(19);
         xpath = m.group(20);
         address = m.group(21);
         additionalInfo = m.group(22);

         return true;
      } else {
         return false;
      }
   }

   /**
    * create a syslog message from this event
    * @param withPri if true add a Priority value. If priValue filed is null, calculate pri using severity and facility numbers
    * @return
    */

   public String toLogEntry(boolean withPri) {
      StringBuffer sb = new StringBuffer();

      //"<187>Feb 12 11:12:08 Hikari notfmgrd[5345]: [1][1][A][5345] [23] Id:1201, Syslog-Severity:3, Perceived-Severity:Major, Name:loss-of-signal, Category:PORT Cause:This alarm is set when there is no signal present on an enabled ethernet interface, Details:Interface operationally down, Xpath:/config/shelf[shelf-id='1']/slot[slot-id='1']/interface/ethernet[port='x2'] Address:/interfaces/interface[name='1/1/x2'], Primary-element:NULL, Value:NULL, Verb:NULL, Session:0, Login:NULL, IpAddress:NULL, SrcManager:NULL";

      if (withPri) {
         if (priValue == null) {
            priValue = calculatePri();
         }
         sb.append("<" + priValue + ">");
      }
      sb.append(month + " ");
      sb.append(day + " ");
      sb.append(timestampStr + " ");
      sb.append(nodename + " ");
      sb.append("notfmgrd[" + processId + "]:");
      sb.append(" [" + shelfId + "]");
      sb.append("[" + slotId + "]");
      sb.append("[" + activeOrStandby + "]");
      sb.append("[" + processId + "]");
      sb.append(" [" + logFacility + "]");
      if (event != null && !event.isEmpty()) {
         sb.append(" " + event);
         sb.append("Id:" + id + ",");
      } else {
         sb.append(" Id:" + id + ",");
      }
      sb.append(" Syslog-Severity:" + syslogSeverity + ",");
      sb.append(" Perceived-Severity:" + perceivedSeverity + ",");
      sb.append(" Name:" + logName + ",");
      sb.append(" Category:" + logCategory + ""); // note no ,
      sb.append(" Cause:" + logCause + ",");
      sb.append(" Details:" + details + ",");
      sb.append(" Xpath:" + xpath + ""); // note no ,
      sb.append(" Address:" + address + ",");
      sb.append(" " + additionalInfo);

      return sb.toString();

   }

   @Override
   public String toString() {
      return "ReadAndParseLogsTest [\n    "
               + "rawMatch=" + rawMatch
               + ",\n    pri=" + pri
               + ",\n    priValue=" + priValue
               + ",\n    month=" + month
               + ",\n    day=" + day
               + ",\n    timestampStr=" + timestampStr
               + ",\n    nodename=" + nodename
               + ",\n    shelfId=" + shelfId
               + ",\n    slotId=" + slotId
               + ",\n    activeOrStandby=" + activeOrStandby
               + ",\n    processId=" + processId
               + ",\n    logFacility=" + logFacility
               + ",\n    event=" + event
               + ",\n    id=" + id
               + ",\n    syslogSeverity=" + syslogSeverity
               + ",\n    perceivedSeverity=" + perceivedSeverity
               + ",\n    logName=" + logName
               + ",\n    logCategory=" + logCategory
               + ",\n    logCause=" + logCause
               + ",\n    details=" + details
               + ",\n    xpath=" + xpath
               + ",\n    address=" + address
               + ",\n    additionalInfo=" + additionalInfo
               + "]";
   }

}
