package org.opennms.rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.Test;

import org.opennms.xmlns.xsd.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

public class OnmsRestClient {

   public static Logger LOG = LoggerFactory.getLogger(OnmsRestClient.class); //slf4j logger

   private String onmsRestUrl = null;
   private String username = null;
   private String password = null;

   public OnmsRestClient(String onmsRestUrl, String username, String password) {
      super();
      this.onmsRestUrl = onmsRestUrl;
      this.username = username;
      this.password = password;
   }

   // see https://stackoverflow.com/questions/4121722/how-to-make-jersey-to-use-slf4j-instead-of-jul
   private static class JulFacade extends java.util.logging.Logger {
      JulFacade() {
         super("Jersey", null);
      }

      @Override
      public void info(String msg) {
         LOG.debug(msg);
      }
   }

   public void postEvent(Event event) {
      HttpAuthenticationFeature basicAuthFeature = HttpAuthenticationFeature.basicBuilder()
               .nonPreemptive()
               .credentials(username, password)
               .build();

      ClientConfig clientConfig = new ClientConfig();
      clientConfig.register(basicAuthFeature);
      clientConfig.register(new LoggingFilter(new JulFacade(), true));

      // note jackson not working correctly - json generated which doesn't parse in opennms
      // MediaType.APPLICATION_JSON
      // see https://github.com/FasterXML/jackson-modules-base/tree/master/jaxb
      //      ObjectMapper objectMapper = new ObjectMapper();
      //      JaxbAnnotationModule module = new JaxbAnnotationModule();
      //      objectMapper.registerModule(module);
      //      clientConfig.register(new JacksonJsonProvider(objectMapper));

 
      Client client = ClientBuilder.newClient(clientConfig);
      // client.register(new LoggingFilter(new JulFacade(), true));

      Entity<Event> entity = Entity.entity(event, MediaType.APPLICATION_XML);

      Response response = client
               .target(onmsRestUrl)
               .request(MediaType.APPLICATION_XML)
               .post(entity);

      LOG.debug("RESONSE: " + response.getStatus() + " " + response.getStatusInfo());

      response.close();
      client.close();

   }

}
