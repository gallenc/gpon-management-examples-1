package org.opennms.rest.client;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.opennms.rest.model.Event;
import org.opennms.rest.model.EventList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class EventListSender {
   public static Logger LOG = LoggerFactory.getLogger(EventListSender.class); //slf4j logger
   
   public static final String USE_DIFFERNCE="usedifference";
   
   private static final Long DEFAULT_DELAY= 100L;  // 100 ms
   
   private String delay = null;   
   private String onmsRestUrl = null;
   private String username = null;
   private String password = null;

   public EventListSender(String onmsRestUrl, String username, String password, String delay) {
      super();
      this.onmsRestUrl = onmsRestUrl;
      this.username = username;
      this.password = password;
      this.delay=delay;
   }

   public void sendEventFile(File eventFile) throws StreamReadException, DatabindException, IOException {
      LOG.debug("sending events from file:"+eventFile.getAbsolutePath());
      ObjectMapper objectMapper = new ObjectMapper();
      
      // if properties not mapped - ignore
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      
      EventList eventList = objectMapper.readValue( eventFile, EventList.class);
      sendEventList(eventList);
   }

   public void sendEventList(EventList eventList) {
      LOG.debug("delay setting: "+delay + " events to send:"+eventList.getEvent().size());
      Long lastEventTime = null;

      for(Event jsonEvent : eventList.getEvent()) {
         org.opennms.xmlns.xsd.event.Event xmlEvent = EventMapper.toXmlEvent(jsonEvent);
         OnmsRestClient client = new OnmsRestClient(onmsRestUrl, username, password);
         client.postEvent(xmlEvent);
         
         Date currentTime = jsonEvent.getTime();
         
         Long delayTime = DEFAULT_DELAY;
         if (USE_DIFFERNCE.equals(delay) && currentTime!=null) {
            if (lastEventTime!=null) {
               delayTime = currentTime.getTime() - lastEventTime;
            } 
            lastEventTime = currentTime.getTime();
            if(delayTime> 1000*60*1) {
               LOG.debug("differnce delay "+delayTime+" ms too big between events. Using Default delay "+DEFAULT_DELAY+" ms");
               delayTime = DEFAULT_DELAY;
            } else {
               LOG.debug("use differnce delay "+delayTime+" ms between events");
            }
         } else if (! USE_DIFFERNCE.equals(delay) && delay != null) {
            delayTime = Long.valueOf(delay);
         }

         
         LOG.debug("waiting "+delayTime+" ms between events");
         try {
            Thread.sleep(delayTime);
        } catch (InterruptedException e) { }
         
         
      }
      
   }
   
   

}
