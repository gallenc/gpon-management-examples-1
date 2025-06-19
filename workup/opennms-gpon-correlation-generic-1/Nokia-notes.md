# Nokia Parsing notes


## this would match all alarms
```
         <match type="regex" expression="APP_VERSION:(.*?),MODULE_NAME:(.*?),ENTITY_NAME:(.*?),ENTITY_TYPE:(.*?),alarm-type-id:(.*?),event-time:(.*?),perceived-severity:critical,alarm-text:((?:.*?)Serial-Number=(.*?),(?:.*)CT-Name=(.*)|(.*))" />
```
 matches both:
 
 ```
APP_VERSION:2212.640,MODULE_NAME:alarm,ENTITY_NAME:12092,ENTITY_TYPE:interface,alarm-type-id:absence-of-phy,event-time:2024-01-10T13:15:05+00:00,perceived-severity:major,alarm-text:Serial-Number=ALCLFCFFFFFF, Reg-ID=, CT-Name=LT2.test301-olt-101_pon2_CTERM_XGS

APP_VERSION:2212.640,MODULE_NAME:alarm,ENTITY_NAME:test301-olt-102-pon1-ont3/ENET_UNI_10GE,ENTITY_TYPE:onu/interface,alarm-type-id:lan-los,event-time:2023-07-25T14:03:58+00:00,perceived-severity:cleared,alarm-text:LAN-LOS (No carrier at the Ethernet UNI)
```

this would match only alarms with serial number and olt

```
         <match type="regex" expression="APP_VERSION:(.*?),MODULE_NAME:(.*?),ENTITY_NAME:(.*?),ENTITY_TYPE:(.*?),alarm-type-id:(.*?),event-time:(.*?),perceived-severity:critical,alarm-text:((?:.*?)Serial-Number=(.*?),(?:.*)CT-Name=(.*))" />


```