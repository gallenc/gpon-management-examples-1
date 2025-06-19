   /*
    * this class uses code from https://github.com/jenkinsci/syslog-java-client/
    * Copyright 2010-2014, CloudBees Inc.
    *
    * Licensed under the Apache License, Version 2.0 (the "License"); you may not
    * use this file except in compliance with the License. You may obtain a copy of
    * the License at
    *
    * http://www.apache.org/licenses/LICENSE-2.0
    *
    * Unless required by applicable law or agreed to in writing, software
    * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
    * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
    * License for the specific language governing permissions and limitations under
    * the License.
    */

package org.opennms.test.application.datagram.syslog;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public enum SyslogSeverity {

   /**
    * Syslog severity as defined in
    * <a href="https://tools.ietf.org/html/rfc5424">RFC 5424 - The Syslog Protocol</a>.
    */

   /**
    * Emergency: system is unusable, numerical code 0.
    */
   EMERGENCY(0, "EMERGENCY"),
   /**
    * Alert: action must be taken immediately, numerical code 1.
    */
   ALERT(1, "ALERT"),
   /**
    * Critical: critical conditions, numerical code 2.
    */
   CRITICAL(2, "CRITICAL"),
   /**
    * Error: error conditions, numerical code 3.
    */
   ERROR(3, "ERROR"),
   /**
    * Warning: warning conditions, numerical code 4.
    */
   WARNING(4, "WARNING"),
   /**
    * Notice: normal but significant condition, numerical code 5.
    */
   NOTICE(5, "NOTICE"),
   /**
    * Informational: informational messages, numerical code 6.
    */
   INFORMATIONAL(6, "INFORMATIONAL"),
   /**
    * Debug: debug-level messages, numerical code 7.
    */
   DEBUG(7, "DEBUG");

   // mapping
   private final static Map<String, SyslogSeverity> severityFromLabel = new HashMap<>();
   private final static Map<Integer, SyslogSeverity> severityFromNumericalCode = new HashMap<>();

   static {
      for (SyslogSeverity severity : SyslogSeverity.values()) {
         severityFromLabel.put(severity.label, severity);
         severityFromNumericalCode.put(severity.numericalCode, severity);
      }
   }

   private final int numericalCode;

   private final String label;

   SyslogSeverity(int numericalCode, String label) {
      if (label == null)
         throw new IllegalArgumentException("label annot be null");
      this.numericalCode = numericalCode;
      this.label = label;
   }

   public static SyslogSeverity fromNumericalCode(int numericalCode) throws IllegalArgumentException {
      SyslogSeverity severity = severityFromNumericalCode.get(numericalCode);
      if (severity == null) {
         throw new IllegalArgumentException("Invalid severity '" + numericalCode + "'");
      }
      return severity;
   }

   public static SyslogSeverity fromLabel(String label) throws IllegalArgumentException {
      if (label == null || label.isEmpty())
         return null;

      SyslogSeverity severity = severityFromLabel.get(label.toUpperCase());
      if (severity == null) {
         throw new IllegalArgumentException("Invalid severity '" + label + "'");
      }
      return severity;
   }


   public int numericalCode() {
      return numericalCode;
   }


   public String label() {
      return label;
   }


   public static Comparator<SyslogSeverity> comparator() {
      return Comparator.comparingInt(s -> s.numericalCode);
   }
}
