# Example events

```
GET http://localhost:8980/opennms/rest/events/5317

<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<event display="Y" log="Y" id="5317" severity="MINOR">
    <createTime>2024-03-20T06:40:29.910-04:00</createTime>
    <description>&lt;p&gt;Calix/Axos ONT Alarm Name: high-laser-bias Cause: High laser bias.&lt;br&gt;
         Node ID: 317&lt;br&gt;
         Host: 212064&lt;br&gt;
         Interface:
         172.20.0.248 &lt;br&gt;
         Process: notfmgrd &lt;br&gt;
         PID: 6203 &lt;br&gt;
         Severity: Minor &lt;br&gt;
         Process:  &lt;br&gt;
         Service
         (facility):  &lt;br&gt;
         &lt;br&gt;

         ShelfId:  &lt;br&gt;
         SlotId:  &lt;br&gt;
         ActiveOrStandby:  &lt;br&gt;
         ProcessId:  &lt;br&gt;
         LogFacility:  &lt;br&gt;
         Event:  &lt;br&gt;
         Id:  &lt;br&gt;
         SyslogSeverity:
          &lt;br&gt;
         PerceivedSeverity: MINOR &lt;br&gt;
         LogName: high-laser-bias &lt;br&gt;
         LogCategory: PON
         &lt;br&gt;
         LogCause: High laser bias. &lt;br&gt;
         Details: SerialNo=E7D3FA &lt;br&gt;
         Xpath: /config/system/ont[ont-id=&amp;#39;212064&amp;#39;] &lt;br&gt;
         Address: NULL &lt;br&gt;
         AdditionalInfo:
         Primary-element:NULL, Value:NULL, Verb:NULL, Session:0, Login:NULL, IpAddress:NULL, SrcManager:NULL, Secondary-element:NULL &lt;br&gt;
         &lt;br&gt;
         &lt;a href="event/detail.jsp?id=5316"&gt;Original Event ID 5316&lt;/a&gt;&lt;br&gt;
         &lt;br&gt;
         Message: Perceived-Severity:MINOR, Name:high-laser-bias, Category:PON Cause:High laser bias., Details:SerialNo=E7D3FA, Xpath:/config/system/ont[ont-id=&amp;#39;212064&amp;#39;] Address:NULL, Primary-element:NULL, Value:NULL, Verb:NULL, Session:0, Login:NULL, IpAddress:NULL, SrcManager:NULL, Secondary-element:NULL &lt;br&gt;
         &lt;br&gt;
         Raw Syslog Message:  &lt;br&gt;
         &lt;/p&gt;</description>
    <host>horizon</host>
    <logMessage>
         &lt;p&gt;Calix/Axos ONT Alarm Name: high-laser-bias Cause: High laser bias.&lt;/p&gt;
      </logMessage>
    <parameters>
        <parameter name="hostname" value="glo204-olt-1" type="string"/>
        <parameter name="rawSyslogmessage" value="" type="string"/>
        <parameter name="syslogmessage" value="Perceived-Severity:MINOR, Name:high-laser-bias, Category:PON Cause:High laser bias., Details:SerialNo=E7D3FA, Xpath:/config/system/ont[ont-id='212064'] Address:NULL, Primary-element:NULL, Value:NULL, Verb:NULL, Session:0, Login:NULL, IpAddress:NULL, SrcManager:NULL, Secondary-element:NULL" type="string"/>
        <parameter name="severity" value="Error" type="string"/>
        <parameter name="timestamp" value="Feb 28 21:36:00" type="string"/>
        <parameter name="process" value="notfmgrd" type="string"/>
        <parameter name="service" value="kernel" type="string"/>
        <parameter name="processid" value="6203" type="string"/>
        <parameter name="group1" value="MINOR" type="string"/>
        <parameter name="group2" value="high-laser-bias" type="string"/>
        <parameter name="group3" value="PON" type="string"/>
        <parameter name="group4" value="High laser bias." type="string"/>
        <parameter name="group5" value="SerialNo=E7D3FA" type="string"/>
        <parameter name="group6" value="/config/system/ont[ont-id='212064']" type="string"/>
        <parameter name="group7" value="NULL" type="string"/>
        <parameter name="group8" value="Primary-element:NULL, Value:NULL, Verb:NULL, Session:0, Login:NULL, IpAddress:NULL, SrcManager:NULL, Secondary-element:NULL" type="string"/>
        <parameter name="PerceivedSeverity" value="MINOR" type="string"/>
        <parameter name="LogName" value="high-laser-bias" type="string"/>
        <parameter name="LogCategory" value="PON" type="string"/>
        <parameter name="LogCause" value="High laser bias." type="string"/>
        <parameter name="Details" value="SerialNo=E7D3FA" type="string"/>
        <parameter name="Xpath" value="/config/system/ont[ont-id='212064']" type="string"/>
        <parameter name="Address" value="NULL" type="string"/>
        <parameter name="AdditionalInfo" value="Primary-element:NULL, Value:NULL, Verb:NULL, Session:0, Login:NULL, IpAddress:NULL, SrcManager:NULL, Secondary-element:NULL" type="string"/>
        <parameter name="originalEventId" value="5316" type="string"/>
    </parameters>
    <source>event-translator</source>
    <time>2024-02-28T16:36:00-05:00</time>
    <uei>uei.opennms.org/Translator/Calix/Axos/ONT/Alarm/Raise</uei>
    <ipAddress>172.20.0.248</ipAddress>
    <nodeId>317</nodeId>
    <nodeLabel>212064</nodeLabel>
</event>
```

same event in json

```
{
    "serviceType": null,
    "ifIndex": null,
    "id": 5317,
    "nodeId": 317,
    "nodeLabel": "212064",
    "uei": "uei.opennms.org/Translator/Calix/Axos/ONT/Alarm/Raise",
    "time": 1709156160000,
    "host": "horizon",
    "source": "event-translator",
    "ipAddress": "172.20.0.248",
    "parameters": [
        {
            "name": "hostname",
            "value": "glo204-olt-1",
            "type": "string"
        },
        {
            "name": "rawSyslogmessage",
            "value": "",
            "type": "string"
        },
        {
            "name": "syslogmessage",
            "value": "Perceived-Severity:MINOR, Name:high-laser-bias, Category:PON Cause:High laser bias., Details:SerialNo=E7D3FA, Xpath:/config/system/ont[ont-id='212064'] Address:NULL, Primary-element:NULL, Value:NULL, Verb:NULL, Session:0, Login:NULL, IpAddress:NULL, SrcManager:NULL, Secondary-element:NULL",
            "type": "string"
        },
        {
            "name": "severity",
            "value": "Error",
            "type": "string"
        },
        {
            "name": "timestamp",
            "value": "Feb 28 21:36:00",
            "type": "string"
        },
        {
            "name": "process",
            "value": "notfmgrd",
            "type": "string"
        },
        {
            "name": "service",
            "value": "kernel",
            "type": "string"
        },
        {
            "name": "processid",
            "value": "6203",
            "type": "string"
        },
        {
            "name": "group1",
            "value": "MINOR",
            "type": "string"
        },
        {
            "name": "group2",
            "value": "high-laser-bias",
            "type": "string"
        },
        {
            "name": "group3",
            "value": "PON",
            "type": "string"
        },
        {
            "name": "group4",
            "value": "High laser bias.",
            "type": "string"
        },
        {
            "name": "group5",
            "value": "SerialNo=E7D3FA",
            "type": "string"
        },
        {
            "name": "group6",
            "value": "/config/system/ont[ont-id='212064']",
            "type": "string"
        },
        {
            "name": "group7",
            "value": "NULL",
            "type": "string"
        },
        {
            "name": "group8",
            "value": "Primary-element:NULL, Value:NULL, Verb:NULL, Session:0, Login:NULL, IpAddress:NULL, SrcManager:NULL, Secondary-element:NULL",
            "type": "string"
        },
        {
            "name": "PerceivedSeverity",
            "value": "MINOR",
            "type": "string"
        },
        {
            "name": "LogName",
            "value": "high-laser-bias",
            "type": "string"
        },
        {
            "name": "LogCategory",
            "value": "PON",
            "type": "string"
        },
        {
            "name": "LogCause",
            "value": "High laser bias.",
            "type": "string"
        },
        {
            "name": "Details",
            "value": "SerialNo=E7D3FA",
            "type": "string"
        },
        {
            "name": "Xpath",
            "value": "/config/system/ont[ont-id='212064']",
            "type": "string"
        },
        {
            "name": "Address",
            "value": "NULL",
            "type": "string"
        },
        {
            "name": "AdditionalInfo",
            "value": "Primary-element:NULL, Value:NULL, Verb:NULL, Session:0, Login:NULL, IpAddress:NULL, SrcManager:NULL, Secondary-element:NULL",
            "type": "string"
        },
        {
            "name": "originalEventId",
            "value": "5316",
            "type": "string"
        }
    ],
    "createTime": 1710931229910,
    "description": "<p>Calix/Axos ONT Alarm Name: high-laser-bias Cause: High laser bias.<br>\n         Node ID: 317<br>\n         Host: 212064<br>\n         Interface:\n         172.20.0.248 <br>\n         Process: notfmgrd <br>\n         PID: 6203 <br>\n         Severity: Minor <br>\n         Process:  <br>\n         Service\n         (facility):  <br>\n         <br>\n\n         ShelfId:  <br>\n         SlotId:  <br>\n         ActiveOrStandby:  <br>\n         ProcessId:  <br>\n         LogFacility:  <br>\n         Event:  <br>\n         Id:  <br>\n         SyslogSeverity:\n          <br>\n         PerceivedSeverity: MINOR <br>\n         LogName: high-laser-bias <br>\n         LogCategory: PON\n         <br>\n         LogCause: High laser bias. <br>\n         Details: SerialNo=E7D3FA <br>\n         Xpath: /config/system/ont[ont-id=&#39;212064&#39;] <br>\n         Address: NULL <br>\n         AdditionalInfo:\n         Primary-element:NULL, Value:NULL, Verb:NULL, Session:0, Login:NULL, IpAddress:NULL, SrcManager:NULL, Secondary-element:NULL <br>\n         <br>\n         <a href=\"event/detail.jsp?id=5316\">Original Event ID 5316</a><br>\n         <br>\n         Message: Perceived-Severity:MINOR, Name:high-laser-bias, Category:PON Cause:High laser bias., Details:SerialNo=E7D3FA, Xpath:/config/system/ont[ont-id=&#39;212064&#39;] Address:NULL, Primary-element:NULL, Value:NULL, Verb:NULL, Session:0, Login:NULL, IpAddress:NULL, SrcManager:NULL, Secondary-element:NULL <br>\n         <br>\n         Raw Syslog Message:  <br>\n         </p>",
    "logMessage": "\n         <p>Calix/Axos ONT Alarm Name: high-laser-bias Cause: High laser bias.</p>\n      ",
    "log": "Y",
    "display": "Y",
    "severity": "MINOR"
}
```

post event - note different format https://github.com/OpenNMS/opennms/blob/develop/features/events/api/src/main/java/org/opennms/netmgt/xml/event/Event.java

```



{
    "uei": "uei.opennms.org/Translator/Calix/Axos/ONT/Alarm/Raise",
    "source": "event-translator",
    "host": "horizon",
    "severity": "MAJOR",
    "descr": "if ommitted the default description is used from event definition",
    "logmsg": {
        "value": "this is hre value of the log message into the log"
    },
    "parms": [
        {
            "parmName": "hostname",
            "value": "glo204-olt-1"
        }
    ]
}
```