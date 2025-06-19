package org.opennms.rest.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*

        {
            "serviceType": null,
            "ifIndex": null,
            "id": 7969,
            "nodeId": 115,
            "nodeLabel": "ket1-olt-1",
            "uei": "uei.opennms.org/internal/provisiond/nodeScanCompleted",
            "time": 1722349611834,
            "source": "Provisiond",
            "parameters": [
                {
                    "name": "foreignSource",
                    "value": "gpon3",
                    "type": "string"
                },
                {
                    "name": "foreignId",
                    "value": "ket1-olt-1",
                    "type": "string"
                }
            ],
            "createTime": 1722349611836,
            "description": "A message from the Provisiond NodeScan lifecycle that a NodeScan has completed:\n            <p>The Node with Id: 115; ForeignSource: gpon3; ForeignId:ket1-olt-1 has\n            completed.</p>\n            Typically the result of a request of an import request or a scheduled/user forced rescan.",
            "logMessage": "\n            <p>The Node with Id: 115; ForeignSource: gpon3; ForeignId:ket1-olt-1 has\n            completed.</p>\n        ",
            "log": "Y",
            "display": "Y",
            "severity": "NORMAL"
        },
*/


public class Event {

   ServiceType serviceType = null;
   String ifIndex = null;
   Integer id = null;
   Integer nodeId = null;
   String nodeLabel = null;
   String uei = null;
   Date time = null;
   String source = null;
   List<Parameter> parameters = new ArrayList<>();
   Date createTime = new Date();
   String description = null;
   String logMessage=null;
   String log = null;
   String display = null;
   String severity = null;
   String host = null;
   String ipAddress = null;
   

   public ServiceType getServiceType() {
      return serviceType;
   }
   public void setServiceType(ServiceType serviceType) {
      this.serviceType = serviceType;
   }
   public String getIfIndex() {
      return ifIndex;
   }
   public void setIfIndex(String ifIndex) {
      this.ifIndex = ifIndex;
   }
   public Integer getId() {
      return id;
   }
   public void setId(Integer id) {
      this.id = id;
   }
   public Integer getNodeId() {
      return nodeId;
   }
   public void setNodeId(Integer nodeId) {
      this.nodeId = nodeId;
   }
   public String getNodeLabel() {
      return nodeLabel;
   }
   public void setNodeLabel(String nodeLabel) {
      this.nodeLabel = nodeLabel;
   }
   public String getUei() {
      return uei;
   }
   public void setUei(String uei) {
      this.uei = uei;
   }
   public Date getTime() {
      return time;
   }
   public void setTime(Date time) {
      this.time = time;
   }
   public String getSource() {
      return source;
   }
   public void setSource(String source) {
      this.source = source;
   }
   public List<Parameter> getParameters() {
      return parameters;
   }
   public void setParameters(List<Parameter> parameters) {
      this.parameters = parameters;
   }
   public Date getCreateTime() {
      return createTime;
   }
   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }
   public String getDescription() {
      return description;
   }
   public void setDescription(String description) {
      this.description = description;
   }
   
   public String getLogMessage() {
      return logMessage;
   }
   public void setLogMessage(String logMessage) {
      this.logMessage = logMessage;
   }
   public String getLog() {
      return log;
   }
   public void setLog(String log) {
      this.log = log;
   }
   public String getDisplay() {
      return display;
   }
   public void setDisplay(String display) {
      this.display = display;
   }
   public String getSeverity() {
      return severity;
   }
   public void setSeverity(String severity) {
      this.severity = severity;
   }

   public String getHost() {
      return host;
   }
   public void setHost(String host) {
      this.host = host;
   }
   public String getIpAddress() {
      return ipAddress;
   }
   public void setIpAddress(String ipAddress) {
      this.ipAddress = ipAddress;
   }
   
   
   @Override
   public String toString() {
      return "Event [serviceType=" + serviceType + ", ifIndex=" + ifIndex + ", id=" + id + ", nodeId=" + nodeId + ", nodeLabel=" + nodeLabel + ", uei=" + uei + ", time=" + time + ", source=" + source
               + ", parameters=" + parameters + ", createTime=" + createTime + ", description=" + description + ", logMessage=" + logMessage + ", log=" + log + ", display=" + display + ", severity="
               + severity + ", host=" + host + ", ipAddress=" + ipAddress + "]";
   }
     

}
