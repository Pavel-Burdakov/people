create table if not exists person (
    id serial not null,
    name varchar not null,
    age int,
    email varchar not null,
    primary key (id),
    UNIQUE (email)
);

create table account (
    account_number serial not null,
    currency varchar(25) not null,
    money_available int not null,
    person_id int references person(id) on delete set null,
    primary key (account_number)
);


create sequence person_sequence
start 1
increment 1
minvalue 0
OWNED BY person.id;