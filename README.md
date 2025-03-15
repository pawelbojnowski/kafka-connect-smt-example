# kafka-connect-smt-example

# Overview

- The **custom-smt-example** module contains an SMT transformation module for Kafka Connect. When you execute
  `mvn package`, the JAR file will also be copied to the directory
  `/custom-smt-infrastructure/vol-kafka-connect-smt-jar`, which is loaded by Kafka Connect.
- The **custom-smt-example** module includes an example producer that sends test messages to two dedicated topics for
  Postgres and Cassandra connectors.
- The **custom-smt-infrastructure** module contains the infrastructure configuration required to run the example for
  `custom-smt-example`.

# Run example

1. Run maven: `mvn clean package`
2. Execute script: `sh custom-smt-infrastructure/runKafka.sh
3. Check:

- If there is a log in the Kafka Connect log...

```
INFO Added aliases 'ProcessTypeTransformation' and 'ProcessType' to plugin 'pl.pb.kafkaconnect.customesmt.ProcessTypeTransformation'
```

- If a list of connectors and topics appears in the terminal...

```
----------------------------------------------------
List connectors:

[
"save_process_data_to_cassandra",
"save_process_data_to_postgres"
]

----------------------------------------------------
List topic:

[
"kc-config",
"kc-status",
"save_process_data_to_postgres",
"save_process_data_to_cassandra",
"kc-offset",
"_schemas"
]
```

4. Now you can
   run [KafkaConnectSmtExample.java](custom-smt-example/src/main/java/pl/pb/kafkaconnectexample/KafkaConnectSmtExample.java)


# Have fun and learn! :) 