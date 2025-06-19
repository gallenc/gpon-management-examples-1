# Log Event parsing examples 

## For calex devices

[CalexAxosLogs regex parsing](../main/CalexAxosLogs.md)


## for unknown logs

[OtherLogs regex parsing](../main/OtherLogs.md)

## to send or receive stand alone logs

build jar

```
 mvn clean install -DskipTests
```


Simple log tester. Either run as server to receive UDP syslogs or as a sender to send a single message

Server: `java -jar syslog-tester-<VERSION>.jar server port`

example: `java -jar syslog-tester-0.0.1-SNAPSHOT.jar server 514`
   

Sender : `java -jar syslog-tester-<VERSION>.jar sender port logmsg`

example: `java -jar syslog-tester-0.0.1-SNAPSHOT.jar send 127.0.0.1 514 'Mar 12 09:01:09 LT1-she503-olt-502 - APP_NAME:alarm_logic_app,APP_VERSION:2212.640,MODULE_NAME:alarm,ENTITY_NAME:624406,ENTITY_TYPE:interface,alarm-type-id:onu-dying-gasp,event-time:2024-03-12T09:01:09+00:00,perceived-severity:major,alarm-text:Serial-Number=ALCLFCA46748, Reg-ID=, CT-Name=LT1.she503-olt-502_pon3_CTERM_XGS'`


