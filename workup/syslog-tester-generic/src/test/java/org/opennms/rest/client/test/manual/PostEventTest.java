package org.opennms.rest.client.test.manual;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.opennms.rest.client.OnmsRestClient;
import org.opennms.xmlns.xsd.event.Event;
import org.opennms.xmlns.xsd.event.Parm;
import org.opennms.xmlns.xsd.event.Parms;
import org.opennms.xmlns.xsd.event.Value;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostEventTest {
   
   public static Logger LOG = LoggerFactory.getLogger( PostEventTest.class); //slf4j logger

   String onmsRestUrl = "http://localhost:8980/opennms/rest/events";
   String username = "admin";
   String password = "admin";


   @Test
   public void test() {

      Event event = new Event();
      event.setUei("uei.opennms.org/Translator/Calex/Axos/ONT/Alarm/Raise");
      event.setSource("external");
      
      event.setParms(new Parms());
      
      Parm e = new Parm();
      e.setParmName("LogName");
      Value value = new Value();
      value.setValue("high_laser_bias");
      e.setValue(value);
      
      System.out.println(event.getParms());
               
      event.getParms().getParm().add(e);
      
      OnmsRestClient onmsClient = new OnmsRestClient(onmsRestUrl,  username, password);
      
      onmsClient.postEvent(event);
      
      
   }

}
