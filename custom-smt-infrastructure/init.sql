CREATE
    EXTENSION pgcrypto WITH SCHEMA public;
create schema if not exists public;

create table public.process
(
    id         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    process_id TEXT,
    name       TEXT,
    type       int
);

INSERT INTO public.process(process_id, name, type)
VALUES ('FC4771A3-101B-4ABA-9155-54794A3CF430', 'Process 1', '1'),
       ('7DC2BF5C-1A9D-4618-A6E7-F6690F0A9588', 'Process 1', '2'),
       ('67DA56B2-6126-4CB6-91D0-1A93A39452EE', 'Process 1', '3');
