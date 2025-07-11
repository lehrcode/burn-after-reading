create table notes (
    id bigint generated always as identity (start with 1 increment by 1) not null primary key,
    key_hash bytea not null unique,
    enc_content bytea not null,
    created_at timestamp(3) with time zone default now() not null
);
