# requisitions



# copy drules rules and restart events

```
in windows / docker desktop use

cd minimal-minion-kafka

docker compose cp container-fs/horizon/opt/opennms-overlay/etc/alarmd/drools-rules.d/gpon-rules.drl horizon:/usr/share/opennms/etc/alarmd/drools-rules.d/gpon-rules.drl

or simply

docker compose cp gpon-rules.drl horizon:/usr/share/opennms/etc/alarmd/drools-rules.d/gpon-rules.drl

to reload eventd use

docker compose exec horizon /usr/share/opennms/bin/send-event.pl uei.opennms.org/internal/reloadDaemonConfig -p 'daemonName alarmd'

```


# gpon3
Adding extra Nokia nodes for nokia logs into mix.
To load use:

```
 docker compose  --profile kafka-client exec horizon /usr/share/opennms/bin/send-event.pl uei.opennms.org/internal/importer/reloadImport -p 'url http://pris:8000/requisitions/gpon3'

```