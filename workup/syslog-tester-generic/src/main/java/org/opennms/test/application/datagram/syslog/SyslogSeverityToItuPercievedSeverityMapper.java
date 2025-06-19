package org.opennms.test.application.datagram.syslog;

public class SyslogSeverityToItuPercievedSeverityMapper {
   /**
    * Mapping percievedSeverity alarm levels to syslog levels - used for <PRI> calculation
    *  https://datatracker.ietf.org/doc/rfc5674/
    *  ITU Perceived Severity      syslog SEVERITY (Name)
    *      Critical                    1 (Alert)
    *      Major                       2 (Critical)
    *      Minor                       3 (Error)
    *      Warning                     4 (Warning)
    *      Indeterminate               5 (Notice)
    *      Cleared                     5 (Notice)
    */
   public static SyslogSeverity mapItuPerceivedSeverity(String percievedSeverity) {
      switch (percievedSeverity.toLowerCase()) {
      case "critical":
         return SyslogSeverity.ALERT;
      case "major":
         return SyslogSeverity.CRITICAL;
      case "minor":
         return SyslogSeverity.ERROR;
      case "warning":
         return SyslogSeverity.WARNING;
      case "indeterminate":
      case "clear":
         return SyslogSeverity.NOTICE;
      default:
         return SyslogSeverity.NOTICE;
      }
   }

}
