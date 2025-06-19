package org.opennms.test.application.datagram.calix.pri.syslog.file.manual;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class ParseEeventFile_COV745_ONT_Raw_events_Cluster1CLEAR extends ParseEventsFilePriTest {
   
   @Before
   public void before() throws IOException{
      
      super.setSourceLogFile("./src/test/resources/syslogevents/cov745_ONT_Raw_events_Cluster1.csv");

      super.setSendEventToOpenNMS(true);

      super.setUseSyslogPri(true);

      super.setChangeLogTimeToToday(true);

      super.setLogTimeFormatChange(true);
      
      super.setReverseOrder(true);

      super.setGenerateTimestamp(true);
      
      super.adjustPerceivedSeverity("Clear");
      
      super.setup();

   }

   @Test
   public void test() {
      super.test1();
   }

}
