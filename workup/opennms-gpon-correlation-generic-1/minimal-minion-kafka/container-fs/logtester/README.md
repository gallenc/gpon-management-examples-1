 ## logtester
 
 build the syslog-tester jar and place in logtester folder
 
 to see logs 
 
 docker compose logs -f logtester
 
 
 to send logs use

Sender : `java -jar syslog-tester-<VERSION>.jar sender port logmsg`

```
docker compose exec logtester java -jar /tmp/syslog-tester-0.0.1-SNAPSHOT.jar send 127.0.0.1 514 'Mar 12 09:01:09 LT1-she503-olt-502 - APP_NAME:alarm_logic_app,APP_VERSION:2212.640,MODULE_NAME:alarm,ENTITY_NAME:624406,ENTITY_TYPE:interface,alarm-type-id:onu-dying-gasp,event-time:2024-03-12T09:01:09+00:00,perceived-severity:major,alarm-text:Serial-Number=ALCLFCA46748, Reg-ID=, CT-Name=LT1.she503-olt-502_pon3_CTERM_XGS'
```
  
to opennms horizon use

```
docker compose exec logtester java -jar /tmp/syslog-tester-0.0.1-SNAPSHOT.jar send horizon 10514 'Mar 12 09:01:09 LT1-she503-olt-502 - APP_NAME:alarm_logic_app,APP_VERSION:2212.640,MODULE_NAME:alarm,ENTITY_NAME:624406,ENTITY_TYPE:interface,alarm-type-id:onu-dying-gasp,event-time:2024-03-12T09:01:09+00:00,perceived-severity:major,alarm-text:Serial-Number=ALCLFCA46748, Reg-ID=, CT-Name=LT1.she503-olt-502_pon3_CTERM_XGS'
```
