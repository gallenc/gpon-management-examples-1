package org.opennms.test.application.datagram.syslog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NokiaEventLogFull {

   private String rawMatch = null;
   private String pri = null;
   private String priValue = null;
   private String month = null;
   private String day = null;
   private String timestampStr = null;
   private String nodename = null;
   private String procId = null;
   private String appName = null;
   private String appVersion = null;
   private String moduleName = null;
   private String entityName = null;
   private String entityType = null;
   private String alarmTypeId = null;
   private String eventTime = null;
   private String perceivedSeverity = null;
   private String alarmText = null;

   private String serialNumber = null;
   
   private String parsedSerialNumber = null;

   public boolean parseLogEntry(String logEntry) {
      // (?s)(<(.*?)>|)(\D{3})\s(\d|\d{2})\s(.{8})\s(.*?)\s(.*?)APP_NAME:(.*?),APP_VERSION:(.*?),MODULE_NAME:(.*?),ENTITY_NAME:(.*?),ENTITY_TYPE:(.*?),alarm-type-id:(.*?),event-time:(.*?),perceived-severity:(.*?),alarm-text:(.*)
      String pattern = "(?s)(<(.*?)>|)(\\D{3})\\s(\\d|\\d{2})\\s(.{8})\\s(.*?)\\s(.*?)APP_NAME:(.*?),APP_VERSION:(.*?),MODULE_NAME:(.*?),ENTITY_NAME:(.*?),ENTITY_TYPE:(.*?),alarm-type-id:(.*?),event-time:(.*?),perceived-severity:(.*?),alarm-text:(.*)";

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
         procId = m.group(7);
         appName = m.group(8);
         appVersion = m.group(9);
         moduleName = m.group(10);
         entityName = m.group(11);
         entityType = m.group(12);
         alarmTypeId = m.group(13);
         eventTime = m.group(14);
         perceivedSeverity = m.group(15);
         alarmText = m.group(16);

         // this gets the initial serial number from the log but allows substitution
         serialNumber = extractSerialNumber(alarmText);
         parsedSerialNumber = serialNumber;

         return true;
      } else {
         return false;
      }
   }

   public String extractSerialNumber(String alarmText) {
      //APP_VERSION:(.*?),MODULE_NAME:(.*?),ENTITY_NAME:(.*?),ENTITY_TYPE:(.*?),alarm-type-id:(.*?),event-time:(.*?),perceived-severity:critical,alarm-text:((?:.*?)Serial-Number=(.*?),(?:.*)CT-Name=(.*))
      String alarmTextPattern = "((?:.*?)Serial-Number=(.*?),(?:.*)CT-Name=(.*))";

      // Create a Pattern object
      Pattern atp = Pattern.compile(alarmTextPattern);

      // Now create matcher object.
      Matcher m = atp.matcher(alarmText);

      if (m.find()) {
         String serialNumber = m.group(2);
         return serialNumber;

      } else {
         return null;
      }
   }
   
   public String substituteSerialNumber(String currentSerialNumber, String newSerialNumber, String alarmText) {
      
      if(currentSerialNumber==null || currentSerialNumber.isEmpty()) return alarmText;
      
      if(newSerialNumber == null) return alarmText;
      
      String newAlarmText = alarmText.replace(currentSerialNumber, newSerialNumber);
      
      return newAlarmText;
   }

   public String getRawMatch() {
      return rawMatch;
   }

   public void setRawMatch(String rawMatch) {
      this.rawMatch = rawMatch;
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

   public String getProcId() {
      return procId;
   }

   public void setProcId(String procId) {
      this.procId = procId;
   }

   public String getAppName() {
      return appName;
   }

   public void setAppName(String appName) {
      this.appName = appName;
   }

   public String getAppVersion() {
      return appVersion;
   }

   public void setAppVersion(String appVersion) {
      this.appVersion = appVersion;
   }

   public String getModuleName() {
      return moduleName;
   }

   public void setModuleName(String moduleName) {
      this.moduleName = moduleName;
   }

   public String getEntityName() {
      return entityName;
   }

   public void setEntityName(String entityName) {
      this.entityName = entityName;
   }

   public String getEntityType() {
      return entityType;
   }

   public void setEntityType(String entityType) {
      this.entityType = entityType;
   }

   public String getAlarmTypeId() {
      return alarmTypeId;
   }

   public void setAlarmTypeId(String alarmTypeId) {
      this.alarmTypeId = alarmTypeId;
   }

   public String getEventTime() {
      return eventTime;
   }

   public void setEventTime(String eventTime) {
      this.eventTime = eventTime;
   }

   public String getPerceivedSeverity() {
      return perceivedSeverity;
   }

   public void setPerceivedSeverity(String perceivedSeverity) {
      this.perceivedSeverity = perceivedSeverity;
   }

   public String getAlarmText() {
      return alarmText;
   }

   public void setAlarmText(String alarmText) {
      this.alarmText = alarmText;
   }

   public String getPri() {
      return pri;
   }

   public String getPriValue() {
      return priValue;
   }

   public String getSerialNumber() {
      return serialNumber;
   }
   
   public void setSerialNumber(String serialNumber) {
      this.serialNumber = serialNumber;
   }


   /**
    * create a syslog message from this event
    * @param withPri if true add a Priority value. If priValue filed is null, calculate pri using severity and facility numbers
    * @return
    */

   public String toLogEntry(boolean withPri) {
      StringBuffer sb = new StringBuffer();

      //Feb 28 16:35:13 LT1-blk1-olt-301 - APP_NAME:alarm_logic_app,APP_VERSION:2212.640,MODULE_NAME:alarm,ENTITY_NAME:ALCLFCA45C31,ENTITY_TYPE:rssi-onu,alarm-type-id:onu-upstream-rx-power-exceed-threshold,event-time:2024-02-28T16:35:13+00:00,perceived-severity:minor,alarm-text:low-alarm < onu-upstream-rx-power(-31.5 dBm) < low-warning, OLT xFP operational limits exceeded"

      // NOTE THIS DOESNT WORK WITH PERCEIVED SEVERITY minor or cleared
      if (withPri) {
         if (priValue == null) {
            try {

               SyslogSeverity syslogSeverity = SyslogSeverityToItuPercievedSeverityMapper.mapItuPerceivedSeverity(perceivedSeverity);
               int severity = syslogSeverity.numericalCode();
               ;
               int facility = SyslogFacility.USER.numericalCode();
               int priValueNo = (facility * 8) + severity;
               priValue = Integer.toString(priValueNo);
            } catch (Exception ex) {
               ex.printStackTrace();
               priValue = "000"; // if unable to create a pri
            }
         }
         sb.append("<" + priValue + ">");
      }
      sb.append(month + " ");
      sb.append(day + " ");
      sb.append(timestampStr + " ");
      sb.append(nodename + " ");
      sb.append(procId); // NOTE procid has last space
      sb.append("APP_NAME:" + appName + ",");
      sb.append("APP_VERSION:" + appVersion + ",");
      sb.append("MODULE_NAME:" + moduleName + ",");
      sb.append("ENTITY_NAME:" + entityName + ",");
      sb.append("ENTITY_TYPE:" + entityType + ",");
      sb.append("alarm-type-id:" + alarmTypeId + ",");
      sb.append("event-time:" + eventTime + ",");
      sb.append("perceived-severity:" + perceivedSeverity + ",");
      
      alarmText = substituteSerialNumber(parsedSerialNumber, serialNumber, alarmText);
      sb.append("alarm-text:" + alarmText);

      return sb.toString();

   }

   @Override
   public String toString() {
      return "NokiaEventLogFull ["
               + "\n    rawMatch=" + rawMatch
               + ",\n    pri=" + pri
               + ",\n    priValue=" + priValue
               + ",\n    month=" + month
               + ",\n    day=" + day
               + ",\n    timestampStr=" + timestampStr
               + ",\n    nodename=" + nodename
               + ",\n    procId=" + procId
               + ",\n    appName=" + appName
               + ",\n    appVersion=" + appVersion
               + ",\n    moduleName=" + moduleName
               + ",\n    entityName=" + entityName
               + ",\n    entityType=" + entityType
               + ",\n    alarmTypeId=" + alarmTypeId
               + ",\n    eventTime=" + eventTime
               + ",\n    perceivedSeverity=" + perceivedSeverity
               + ",\n    alarmText=" + alarmText + "]";
   }

}
