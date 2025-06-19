# Nokia Logs

see https://regex101.com/

This matches logs in the events file - not confirming to CALEX format (probably nokia)
```
Feb 28 16:35:13 LT1-blk1-olt-301 - APP_NAME:alarm_logic_app,APP_VERSION:2212.640,MODULE_NAME:alarm,ENTITY_NAME:ALCLFCA45C31,ENTITY_TYPE:rssi-onu,alarm-type-id:onu-upstream-rx-power-exceed-threshold,event-time:2024-02-28T16:35:13+00:00,perceived-severity:minor,alarm-text:low-alarm < onu-upstream-rx-power(-31.5 dBm) < low-warning, OLT xFP operational limits exceeded
```

find priority where field may not be present
(?s)(<(.*?)>|)

find month day and time
>(\D{3})\s(\d{2})\s(.{8})

## full regex example

Full regex - this will match whether or not there is a PRI field

```
sample log
Feb 28 16:35:13 LT1-blk1-olt-301 - APP_NAME:alarm_logic_app,APP_VERSION:2212.640,MODULE_NAME:alarm,ENTITY_NAME:ALCLFCA45C31,ENTITY_TYPE:rssi-onu,alarm-type-id:onu-upstream-rx-power-exceed-threshold,event-time:2024-02-28T16:35:13+00:00,perceived-severity:minor,alarm-text:low-alarm < onu-upstream-rx-power(-31.5 dBm) < low-warning, OLT xFP operational limits exceeded

regex
(?s)(<(.*?)>|)(\D{3})\s(\d|\d{2})\s(.{8})\s(.*?)\s(.*?)APP_NAME:(.*?),APP_VERSION:(.*?),MODULE_NAME:(.*?),ENTITY_NAME:(.*?),ENTITY_TYPE:(.*?),alarm-type-id:(.*?),event-time:(.*?),perceived-severity:(.*?),alarm-text:(.*)
```

| tag | group | value | notes |
| --- | --- | --- | --- |
|     | match 1 (group 0)  | ``` Feb 28 16:35:13 LT1-blk1-olt-301 - APP_NAME:alarm_logic_app,APP_VERSION:2212.640,MODULE_NAME:alarm,ENTITY_NAME:ALCLFCA45C31,ENTITY_TYPE:rssi-onu,alarm-type-id:onu-upstream-rx-power-exceed-threshold,event-time:2024-02-28T16:35:13+00:00,perceived-severity:minor,alarm-text:low-alarm < onu-upstream-rx-power(-31.5 dBm) < low-warning, OLT xFP operational limits exceeded```    |     |
| pri | Group 1 | empty string | |
| priValue | Group 2 | null  |(or null if no PRI |
| month | Group 3  | Feb | |
| day | Group 4 |  28  | (matched for date of 1 character or 2 characters i.e. Feb 1 or Feb 20)|
| timestamp | Group 5 |  16:35:13 | |
| nodename | Group 6 | LT1-blk1-olt-301 | |
| procId | Group 7  | - | this an empty field with - not sure if other values possible |
| appName | Group 8  | alarm_logic_app | |
| appVersion | Group 9  | 2212.640 | |
| moduleName | Group 10  | alarm | |
| entityName | Group 11  | ALCLFCA45C31 | |
| entityType | Group 12  | rssi-onu  | |
| alarmTypeId | Group 13 | onu-upstream-rx-power-exceed-threshold  | |
| eventTime | Group 14 | 2024-02-28T16:35:13+00:00 | |
| perceivedSeverity | Group 15 | minor | |
| alarmText | Group 16 | low-alarm < onu-upstream-rx-power(-31.5 dBm) < low-warning, OLT xFP operational limits exceeded | |
