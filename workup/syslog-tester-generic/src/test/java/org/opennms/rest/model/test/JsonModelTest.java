package org.opennms.rest.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.opennms.rest.model.EventList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class JsonModelTest {
   
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

   @Test
   public void test() throws JsonMappingException, JsonProcessingException {
      
      ObjectMapper objectMapper = new ObjectMapper();
      
      objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

      EventList eventList = objectMapper.readValue( testEventList, EventList.class);
      
      System.out.println("eventList = "+eventList);
      
      String newEventListJson = objectMapper.writeValueAsString(eventList);
      
      System.out.println("newEventListJson = "+newEventListJson);
      
      // check same without indent
      EventList eventlist2 = objectMapper.readValue(newEventListJson, EventList.class);
      String parsedEventList2 = objectMapper.writeValueAsString(eventlist2);
      
      System.out.println("parsedEventList2 = "+parsedEventList2);
      
      assertEquals(parsedEventList2 , newEventListJson);
      
   }

}
