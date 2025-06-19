package org.opennms.rest.client.test.manual;

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

public class EventListSenderTest {
   
   String onmsRestUrl = "http://localhost:8980/opennms/rest/events";
   String username = "admin";
   String password = "admin";

   @Test
   public void sendEventListTest() throws JsonMappingException, JsonProcessingException {
      System.out.println("start of sendEventListTest");
      
      String testEventList = "{\n"
               + "    \"count\": 10,\n"
               + "    \"totalCount\": 5195,\n"
               + "    \"offset\": 0,\n"
               + "    \"event\": [\n"
               + "        {\n"
               + "            \"serviceType\": null,\n"
               + "            \"ifIndex\": null,\n"
               + "            \"id\": 7969,\n"
               + "            \"nodeId\": 115,\n"
               + "            \"nodeLabel\": \"ket1-olt-1\",\n"
               + "            \"uei\": \"uei.opennms.org/internal/provisiond/nodeScanCompleted\",\n"
               + "            \"time\": 1722349611834,\n"
               + "            \"source\": \"Provisiond\",\n"
               + "            \"parameters\": [\n"
               + "                {\n"
               + "                    \"name\": \"foreignSource\",\n"
               + "                    \"value\": \"gpon3\",\n"
               + "                    \"type\": \"string\"\n"
               + "                },\n"
               + "                {\n"
               + "                    \"name\": \"foreignId\",\n"
               + "                    \"value\": \"ket1-olt-1\",\n"
               + "                    \"type\": \"string\"\n"
               + "                }\n"
               + "            ],\n"
               + "            \"createTime\": 1722349611836,\n"
               + "            \"description\": \"A message from the Provisiond NodeScan lifecycle that a NodeScan has completed:\\n            <p>The Node with Id: 115; ForeignSource: gpon3; ForeignId:ket1-olt-1 has\\n            completed.</p>\\n            Typically the result of a request of an import request or a scheduled/user forced rescan.\",\n"
               + "            \"logMessage\": \"\\n            <p>The Node with Id: 115; ForeignSource: gpon3; ForeignId:ket1-olt-1 has\\n            completed.</p>\\n        \",\n"
               + "            \"log\": \"Y\",\n"
               + "            \"display\": \"Y\",\n"
               + "            \"severity\": \"NORMAL\"\n"
               + "        }"
               + "    ]\n"
               + "}";
      
      
      ObjectMapper objectMapper = new ObjectMapper();
      EventList eventList = objectMapper.readValue( testEventList, EventList.class);
      
      
      EventListSender eventListSender = new EventListSender(onmsRestUrl,  username,  password, null);
      
      eventListSender.sendEventList(eventList);
      System.out.println("end of sendEventListTest");
   }
   
   @Test
   public void sendEventFileTest() throws StreamReadException, DatabindException, IOException {
      System.out.println("start of sendEventFileTest");
      EventListSender eventListSender = new EventListSender(onmsRestUrl,  username,  password, EventListSender.USE_DIFFERNCE);
      
      File eventFile= new File("./src/test/resources/jsonevents/testEventFile.json");
      eventListSender.sendEventFile(eventFile);
      System.out.println("end of sendEventFileTest");
   }

}
