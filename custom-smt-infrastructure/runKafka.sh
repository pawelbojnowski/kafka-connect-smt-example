#!/bin/bash


# Remove existing docker images
ids=$(docker ps -aqf name=kafka-connect-smt-example_.)
for id in $(echo $ids | tr "\n" " "); do
  docker stop  $id
  docker container rm -f $id
done

# Remove existing docker volumes
rm -rf ./volumes

# Create docker
docker-compose up --build -d --remove-orphans

while [[ $(curl -s -H "Content-Type: application/json" -XGET 'http://localhost:8083/connectors') != "[]" ]];
do
  printf "."
  sleep 1
done
sleep 3
echo "\n----------------------------------------------------"
echo "Create connectors:\n"

# Add connector for saving data to postgres
curl -s -H "Content-Type: application/json" -XPOST 'http://localhost:8083/connectors' \
-d '{
      "name": "save_process_data_to_postgres",
      "config": {
        "topics": "save_process_data_to_postgres",
        "connector.class": "io.confluent.connect.jdbc.JdbcSinkConnector",
        "tasks.max": "1",
        "connection.url": "jdbc:postgresql://kafka-connect-smt-example_postgres:5432/postgres",
        "connection.user": "postgres",
        "connection.password": "postgres",
        "connection.ds.pool.size": 5,
        "insert.mode.databaselevel": true,
        "table.name.format": "process",
        "auto.create": "false",
        "auto.evolve": "true",
        "insert.mode": "insert",
        "delete.enabled": "false",
        "schemas.enable": "false",
        "key.converter": "org.apache.kafka.connect.storage.StringConverter",
        "value.converter": "org.apache.kafka.connect.json.JsonConverter",
        "key.converter.schemas.enable": "false",
        "value.converter.schemas.enable": "true",
        "transforms": "ProcessTypeTransformation",
        "transforms.ProcessTypeTransformation.type": "pl.pb.kafkaconnect.customesmt.ProcessTypeTransformation"
      }
    }'  | json_pp

# Add connector for saving data to cassandra
curl -s -H "Content-Type: application/json" -XPOST 'http://localhost:8083/connectors' -d '
{
  "name": "save_process_data_to_cassandra",
  "config": {
    "connector.class": "com.datastax.oss.kafka.sink.CassandraSinkConnector",
    "tasks.max": "1",
    "topics": "save_process_data_to_cassandra",
    "contactPoints": "kafka-connect-smt-example_cassandra",
    "auth.username": "root",
    "auth.password": "root",
    "loadBalancing.localDc": "datacenter1",
    "topic.save_process_data_to_cassandra.kafka_connect_smt_example.process.mapping": "id=key, process_id=value.process_id, name=value.name, type=value.type",
    "key.converter": "org.apache.kafka.connect.storage.StringConverter",
    "value.converter": "org.apache.kafka.connect.json.JsonConverter",
    "key.converter.schemas.enable": "false",
    "value.converter.schemas.enable": "true",
    "fields.whitelist": "process_id, name, type",
    "transforms": "ProcessTypeTransformation",
    "transforms.ProcessTypeTransformation.type": "pl.pb.kafkaconnect.customesmt.ProcessTypeTransformation"
  }
}
'  | json_pp

sleep 2

echo "\n----------------------------------------------------"
echo "List connectors:\n"

curl -s -H "Content-Type: application/json" -XGET 'http://localhost:8083/connectors'  | json_pp

echo "\n----------------------------------------------------"
echo "List topic:\n"
curl -s -H "Content-Type: application/vnd.kafka.v2+json" -XGET 'http://localhost:8082/topics' | json_pp
