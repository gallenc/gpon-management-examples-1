package org.opennms.rest.model;

import java.util.ArrayList;
import java.util.List;

/*
{
    "count": 10,
    "totalCount": 5195,
    "offset": 0,
    "event": [
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

public class EventList {
   
   private Integer count = null;
   private Integer totalCount = null;
   private Integer offset = null;
   private List<Event> event = new ArrayList<>();
   
   public Integer getCount() {
      return count;
   }
   public void setCount(Integer count) {
      this.count = count;
   }
   public Integer getTotalCount() {
      return totalCount;
   }
   public void setTotalCount(Integer totalCount) {
      this.totalCount = totalCount;
   }
   public Integer getOffset() {
      return offset;
   }
   public void setOffset(Integer offset) {
      this.offset = offset;
   }
   public List<Event> getEvent() {
      return event;
   }
   public void setEvent(List<Event> event) {
      this.event = event;
   }
   @Override
   public String toString() {
      return "EventList [count=" + count + ", totalCount=" + totalCount + ", offset=" + offset + ", event=" + event + "]";
   }
   
   
   
}