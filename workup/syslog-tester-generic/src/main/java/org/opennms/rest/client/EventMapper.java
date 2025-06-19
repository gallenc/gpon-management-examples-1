package org.opennms.rest.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opennms.rest.model.Parameter;
import org.opennms.rest.model.ServiceType;
import org.opennms.xmlns.xsd.event.Event;
import org.opennms.xmlns.xsd.event.Parm;
import org.opennms.xmlns.xsd.event.Parms;
import org.opennms.xmlns.xsd.event.Value;

public class EventMapper {
   
   public static org.opennms.xmlns.xsd.event.Event toXmlEvent(org.opennms.rest.model.Event jsonEvent){
      org.opennms.xmlns.xsd.event.Event xmlEvent = new org.opennms.xmlns.xsd.event.Event();
      
      String source = jsonEvent.getSource();
      List<Parameter> parameters = jsonEvent.getParameters();
      Integer id = jsonEvent.getId();
      String uei = jsonEvent.getUei();
      String description = jsonEvent.getDescription();
      String severity = jsonEvent.getSeverity();
      
      ServiceType serviceType = jsonEvent.getServiceType();
      String ifIndex = jsonEvent.getIfIndex();
      Integer nodeId = jsonEvent.getNodeId();
      String nodeLabel = jsonEvent.getNodeLabel();
      Date time = jsonEvent.getTime();

      String logMessage=jsonEvent.getLogMessage();
      String log = jsonEvent.getLog();
      String display = jsonEvent.getDisplay();
      Date createTime = jsonEvent.getCreateTime();
      String host = jsonEvent.getHost();
      String ipAddress =jsonEvent.getIpAddress();

      
      // only mapping basic event information
      xmlEvent.setDbid(id);
      xmlEvent.setUei(uei);
      xmlEvent.setDescr(description);
      xmlEvent.setSource(source);
      xmlEvent.setNodeid((id!=null)? Long.valueOf(id):null);
      xmlEvent.setSeverity((severity!=null) ? severity.toUpperCase() : null);
      xmlEvent.setHost(host);
      xmlEvent.setInterface(ipAddress);
      xmlEvent.setService((serviceType!=null) ? serviceType.getName():null);

      if(parameters!=null) {
         xmlEvent.setParms(new Parms());
         
         for (Parameter parameter: parameters) {
            Parm e = new Parm();
            e.setParmName(parameter.getName());
            Value value = new Value();
            value.setValue(parameter.getValue());
            e.setValue(value);
            xmlEvent.getParms().getParm().add(e);
         }
      }
      

      return xmlEvent;
   }
   
   
   public static org.opennms.rest.model.Event toJsonEvent( org.opennms.xmlns.xsd.event.Event xmlEvent){
      org.opennms.rest.model.Event jsonEvent = new org.opennms.rest.model.Event();
      //TODO MAPPING
      
      
      
      
      return jsonEvent;
   }

}
