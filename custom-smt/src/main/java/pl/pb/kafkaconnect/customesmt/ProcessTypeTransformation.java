package pl.pb.kafkaconnect.customesmt;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.transforms.Transformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ProcessTypeTransformation<R extends ConnectRecord<R>> implements Transformation<R> {

    private static final Logger log = LoggerFactory.getLogger(ProcessTypeTransformation.class);

    private static final String FIELD_NAME = "type";

    private static final Map<String, Integer> STATUS_MAP = new HashMap<>();

    static {
        STATUS_MAP.put("PREPARATION", 1);
        STATUS_MAP.put("EXECUTION", 2);
        STATUS_MAP.put("VERIFICATION", 3);
    }

    @Override
    public R apply(R record) {
        if (record.value() == null) {
            return record;
        }
        log.info("Transformation fo records: {} with schema: {}", record.value(), record.valueSchema());

        Struct struct = (Struct) record.value();
        Schema schema = struct.schema();

        if (schema.field(FIELD_NAME) == null) {
            return record;
        }
        // Create new schema
        Schema newValueSchema = createNewSchema(schema);
        // Create new value (structure) based on new schema
        Struct newValueStruct = createNewStruct(newValueSchema, struct, schema);

        return record.newRecord(record.topic(), record.kafkaPartition(), record.keySchema(), record.key(), newValueSchema, newValueStruct, record.timestamp());
    }

    private Struct createNewStruct(Schema newValueSchema, Struct struct, Schema schema) {
        Struct newValueStruct = new Struct(newValueSchema);
        newValueStruct.put(FIELD_NAME, STATUS_MAP.getOrDefault(struct.getString(FIELD_NAME), -1));
        schema.fields()
                .forEach(field -> {
                    if (!FIELD_NAME.equals(field.name())) {
                        newValueStruct.put(field.name(), struct.get(field.name()));
                    }
                });
        return newValueStruct;
    }

    private Schema createNewSchema(Schema schema) {
        SchemaBuilder schemaBuilder = SchemaBuilder.struct()
                .name(schema.name())
                .field(FIELD_NAME, Schema.INT32_SCHEMA);

        schema.fields()
                .forEach(field -> {
                    if (!FIELD_NAME.equals(field.name())) {
                        schemaBuilder.field(field.name(), field.schema());
                    }
                });
        return schemaBuilder.build();
    }

    @Override
    public ConfigDef config() {
        return new ConfigDef();
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }
}

