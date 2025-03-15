package pl.pb.kafkaconnectexample;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static pl.pb.kafkaconnectexample.KafkaProducerUtil.SAVE_PROCESS_DATA_TO_CASSANDRA;
import static pl.pb.kafkaconnectexample.KafkaProducerUtil.SAVE_PROCESS_DATA_TO_POSTGRES;
import static pl.pb.kafkaconnectexample.KafkaProducerUtil.createProducer;

public class KafkaConnectSmtExample {

    private static final Logger log = LoggerFactory.getLogger(KafkaConnectSmtExample.class);
    public static final String VALUE = """
            {
               "schema": {
                 "type": "struct",
                 "fields": [{
                     "field": "process_id",
                     "type": "string",
                     "parameters": {
                       "format": "uuid"
                     }
                   },
                   {
                     "field": "name",
                     "type": "string",
                     "optional": true
                   },
                   {
                     "field": "type",
                     "type": "string",
                     "optional": true
                   }
                 ],
                 "optional": false,
                 "name": "process"
               },
               "payload": {
                 "process_id": "%s",
                 "name": "Data Processing",
                 "type": "EXECUTION"
               }
             }""";

    public static void main(String[] args) {

        final KafkaProducer<String, String> producer = createProducer();

        //save in to table in Postgres
        send(SAVE_PROCESS_DATA_TO_POSTGRES, producer, UUID.randomUUID().toString(), VALUE.formatted(UUID.randomUUID().toString()));

        //Save in to table in Postgres
        send(SAVE_PROCESS_DATA_TO_CASSANDRA, producer, UUID.randomUUID().toString(), VALUE.formatted(UUID.randomUUID().toString()));

        // flush data - synchronous
        producer.flush();

        // flush and close producer
        producer.close();
    }

    private static void send(final String inputTopic, final KafkaProducer<String, String> producer, final String key, final String value) {

        // create a producer record
        final ProducerRecord<String, String> producerRecord = new ProducerRecord<>(inputTopic, key, value);

        // send data - asynchronous
        producer.send(producerRecord);

        log.info("Sent message for 'recordMessages': {}", producerRecord);
    }
}
