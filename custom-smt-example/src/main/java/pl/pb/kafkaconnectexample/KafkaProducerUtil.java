package pl.pb.kafkaconnectexample;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducerUtil {

    private static final String URL = "localhost:9091";
    public static final String SAVE_PROCESS_DATA_TO_POSTGRES = "save_process_data_to_postgres";
    public static final String SAVE_PROCESS_DATA_TO_CASSANDRA = "save_process_data_to_cassandra";

    private KafkaProducerUtil() {
    }

    public static KafkaProducer createProducer() {
        return new KafkaProducer(getProducerConfig());
    }

    private static Properties getProducerConfig() {
        final Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, URL);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

}
