package org.opennms.test.application.datagram.syslog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NokiaEventLogPartial {

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

   private String alarmText = null;
   

   
   public boolean parseLogEntry(String logEntry) {
      // (?s)(<(.*?)>|)(\D{3})\s(\d|\d{2})\s(.{8})\s(.*?)\s(.*?)APP_NAME:(.*?),APP_VERSION:(.*?),MODULE_NAME:(.*?),(.*)
      String pattern = "(?s)(<(.*?)>|)(\\D{3})\\s(\\d|\\d{2})\\s(.{8})\\s(.*?)\\s(.*?)APP_NAME:(.*?),APP_VERSION:(.*?),MODULE_NAME:(.*?),(.*)";

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
         alarmText = m.group(11);
         
         return true;
      } else {
         return false;
      }
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



   /**
    * create a syslog message from this event
    * @param withPri if true add a Priority value. If priValue filed is null, calculate pri using severity and facility numbers
    * @return
    */
   
   public String toLogEntry(boolean withPri) {
      StringBuffer sb = new StringBuffer();
      
       //Feb 28 16:35:13 LT1-blk1-olt-301 - APP_NAME:alarm_logic_app,APP_VERSION:2212.640,MODULE_NAME:alarm,ENTITY_NAME:ALCLFCA45C31,ENTITY_TYPE:rssi-onu,alarm-type-id:onu-upstream-rx-power-exceed-threshold,event-time:2024-02-28T16:35:13+00:00,perceived-severity:minor,alarm-text:low-alarm < onu-upstream-rx-power(-31.5 dBm) < low-warning, OLT xFP operational limits exceeded"

         sb.append(month+" ");
         sb.append(day+" "); 
         sb.append(timestampStr+" ");
         sb.append(nodename+" ");
         sb.append(procId); // NOTE procid has last space
         sb.append("APP_NAME:"+appName+",");
         sb.append("APP_VERSION:"+appVersion+",");
         sb.append("MODULE_NAME:"+moduleName+",");
         sb.append(alarmText);
      
      return sb.toString();
      
   }

   @Override
   public String toString() {
      return "NokiaEventLogPartial ["
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
               + ",\n    alarmText=" + alarmText + "]";
   }
   
   
   
}
