# notes

THIS VERSION CONTANSE A WORKING DROOLS SITUATION EXAMPLE TO PROPOGATE ALARMS UP A TREE


## reloading eventd configuration for syslog events

```
cd minimal-minion-activemq
docker compose cp ./container-fs/horizon/opt/opennms-overlay/etc/eventconf.xml horizon:/usr/share/opennms/etc/

docker compose cp ./container-fs/horizon/opt/opennms-overlay/etc/events/Calix-Axos.syslog.events.xml  horizon:/usr/share/opennms/etc/events/

## send an event to reload the daemon
docker compose exec horizon /usr/share/opennms/bin/send-event.pl uei.opennms.org/internal/reloadDaemonConfig -p 'daemonName Eventd' 
```

## reloading syslogd configuration

```
cd minimal-minion-kafka

docker compose cp ./container-fs/horizon/opt/opennms-overlay/etc/syslogd-grok-patterns.txt horizon:/usr/share/opennms/etc/

docker compose cp ./container-fs/horizon/opt/opennms-overlay/etc/syslogd-configuration.xml horizon:/usr/share/opennms/etc/

docker compose cp ./container-fs/horizon/opt/opennms-overlay/etc/syslog/Calix-Axos.syslog.xml  horizon:/usr/share/opennms/etc/syslog/


## send an event to reload the daemon
docker compose exec horizon /usr/share/opennms/bin/send-event.pl uei.opennms.org/internal/reloadDaemonConfig -p 'daemonName syslogd' 


## queries to pre load asset table with count of child nodes


--UPDATE assets a SET hdd2= ( SELECT COUNT(*) FROM node pn WHERE pn.nodeparentid = n.nodeid ) FROM node n WHERE a.nodeid=n.nodeid ;

or
WITH updateAssetChildCounts AS ( UPDATE assets a SET hdd2= ( SELECT COUNT(*) FROM node pn WHERE pn.nodeparentid = n.nodeid ) FROM node n WHERE a.nodeid=n.nodeid RETURNING 1)
SELECT COUNT(*) FROM updateAssetChildCounts

to view results

--SELECT n.nodeid, n.nodelabel, n.nodeparentid, a.hdd1, a.hdd2 FROM assets a JOIN node n ON n.nodeid= a.nodeid ORDER BY n.nodeid;


select * from events where eventuei LIKE 'uei.opennms.org/internal/importer%'


select * from events where eventuei LIKE 'uei.opennms.org/Translator/gpon/childCountUpdated%'


TO preload sibling count of nodes
-- WITH updateAssetSiblingCounts AS ( UPDATE assets a SET hdd3= ( SELECT COUNT(*) FROM node pn WHERE pn.nodeparentid = n.nodeparentid ) FROM node n WHERE a.nodeid=n.nodeid RETURNING 1)          SELECT COUNT(*) FROM updateAssetSiblingCounts

TO VIEW 
-- SELECT node.nodeid, assets.assetNumber, assets.hdd1,assets.hdd2,assets.hdd3, nodeparentid FROM assets JOIN node ON assets.nodeid=node.nodeid ORDER BY nodeparentid;
```