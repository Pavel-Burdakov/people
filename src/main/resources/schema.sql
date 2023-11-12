create table if not exists person (
    id bigserial not null,
    name varchar not null,
    age int,
    email varchar not null,
    primary key (id),
    UNIQUE (email)
);