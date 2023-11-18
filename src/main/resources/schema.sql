create table if not exists person (
    id serial not null,
    name varchar not null,
    age int,
    email varchar not null,
    primary key (id),
    UNIQUE (email)
);

create sequence person_sequence
start 0
increment 1
minvalue 0
OWNED BY person.id;