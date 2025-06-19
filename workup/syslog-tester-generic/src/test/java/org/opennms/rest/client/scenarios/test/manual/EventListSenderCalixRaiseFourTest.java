package org.opennms.rest.client.scenarios.test.manual;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.opennms.rest.client.EventListSender;
import org.opennms.rest.model.EventList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class EventListSenderCalixRaiseFourTest {
   
   String onmsRestUrl = "http://localhost:8980/opennms/rest/events";
   String username = "admin";
   String password = "admin";


   
   @Test
   public void sendEventFileTest() throws StreamReadException, DatabindException, IOException {
      System.out.println("start of sendEventFileTest");
      EventListSender eventListSender = new EventListSender(onmsRestUrl,  username,  password, EventListSender.USE_DIFFERNCE);
      
      File eventFile= new File("./src/test/resources/jsonevents/testEventONTRaiseFOUR1.json");
      eventListSender.sendEventFile(eventFile);
      System.out.println("end of sendEventFileTest");
   }

}
