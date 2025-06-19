# Setting up opennms-kafka-producer test environment

Provides a running OpenNMS with a kafka broker and a minion.

(Instructions for testing the chubb-rules are found in [RulesTests.md](../RulesTests.md)  )

Forked from https://github.com/opennms-forge/stack-play

OpenNMS horizon docker images are here
https://hub.docker.com/u/opennms

## setup
Install docker / docker compose of your development machine.
On a PC you can use docker-desktop

the .env file sets the version of OpenNMS to use - currently 29.0.6
(see https://docs.docker.com/compose/environment-variables/)


## To run OpenMMS

```
docker compose up -d
```

eventually you should see the OpenNMS UI at http://localhost:8980
(note if you are usign windows with ipv6 you may need to use http://[::1]:8980)

you should also see the kafka ui at http://localhost:8080/

PS - to end the tests use

```
docker compose down
```
and to also clear the database volumes

```
docker compose down -v
```

you can reach inside the opennms container using

```
docker compose exec horizon bash
```

# To run kafka-client and kafka-web
Two simple containers are provided from the integrationexample1 project.
These containers need to be built before they can be used.

Both containers expect the kafka broker address to be injected to

```
 /tmp/kafkaclient.properties
 ```

If you have built the containers, use the following command with profile to activate or deactivate them in docker-compose

```
docker compose  --profile kafka-client  up -d

docker compose  --profile kafka-client down
```
you can see any messages received by the kafka-client using

```
docker compose  logs -f kafka-client
```

You should also be able to see the state of an alarms ktable on the kafka-web client at

http://localhost:8081

or 

http://[::1]:8081

# To activate and test opennms alarms forwarder.

Based on https://docs.opennms.com/horizon/29/operation/kafka-producer/kafka-producer.html
(see also https://rmoff.net/2018/08/02/kafka-listeners-explained/)

Please note that the kafka broker in docker compose is running on port 29092  not 9092 as described in the docs.

You need to get access to the OpenNMS Karaf shell. 
There is an SSH client installed on the minion image but not the horizon image - so we ssh into the horizon opennms from the minion

note that docker compose sets up the service names broker, horizon, database as dns names so ssh etc can reference these

To manually set up the alarm forwarder following the documentation

```
docker compose exec minion bash
ssh -p 8101 admin@horizon

(ssh -p 8101  -o UserKnownHostsFile=/dev/null  admin@localhost can be used from the docker host while avoiding host signature checking)

admin@opennms()> config:edit org.opennms.features.kafka.producer.client
admin@opennms()> config:property-set bootstrap.servers kafka:29092
admin@opennms()> config:update
admin@opennms()> feature:install opennms-kafka-producer

check feature is installed
admin@opennms()> feature:list | grep opennms-kafka-producer
opennms-kafka-producer                      | 29.0.6            | x        | Started     | opennms-29.0.6                    | OpenNMS :: Kafka :: Producer
```
However we can provide this configuration permanently to OpenNMS by adding the following files to the configuration in the meridian opennms etc directory. (Note - for some reason this should but  doesn't work - you need to configure the image manually)

create the file org.opennms.features.kafka.producer.client.cfg and add contents

```
bootstrap.servers = broker:29092
```

This file is put in the docker compose project

```
container-fs\horizon\opt\opennms-overlay\etc\org.opennms.features.kafka.producer.client.cfg 
```

Also added opennms-kafka-producer feature into org.apache.karaf.features.cfg before the line opennms-karaf-health

```
...
  opennms-search, \
  opennms-kafka-producer, \
  opennms-karaf-health

```

to test alarm list within the karaf cli, use the following karaf cli commands. 
(note if no alarms in system, these lines will be empty).

```
admin@opennms()> opennms:kafka-sync-alarms
Performing synchronization of alarms from the database with those in the ktable.
Executed 0 updates in 3ms.

Number of reduction keys in ktable: 1
Number of reduction keys in the db: 1 (1 alarms total)
admin@opennms()> opennms:kafka-list-alarms
uei.opennms.org/internal/importer/importSuccessful:file:/opt/opennms/etc/imports/Minions.xml
        OpenNMS-defined internal event: importer process successfully completed
```



