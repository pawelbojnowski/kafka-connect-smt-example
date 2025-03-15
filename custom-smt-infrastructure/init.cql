create keyspace kafka_connect_smt_example with replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

use kafka_connect_smt_example;

CREATE TABLE process
(
    id         UUID PRIMARY KEY,
    process_id UUID,
    name       TEXT,
    type       INT
);

INSERT INTO process (id, process_id, name, type)
VALUES (uuid(), FC4771A3-101B-4ABA-9155-54794A3CF430, 'Process 1', 1);

INSERT INTO process (id, process_id, name, type)
VALUES (uuid(), 7DC2BF5C-1A9D-4618-A6E7-F6690F0A9588, 'Process 1', 2);

INSERT INTO process (id, process_id, name, type)
VALUES (uuid(), 67DA56B2-6126-4CB6-91D0-1A93A39452EE, 'Process 1', 3);