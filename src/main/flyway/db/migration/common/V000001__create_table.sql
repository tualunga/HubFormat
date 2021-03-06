create table hbdf_table
(
    id             serial not null
        constraint hbdf_table_pk
            primary key,
    name           varchar(50),
    date           date,
    xml            text,
    height         double precision,
    type_of_device varchar(50),
    age            integer,
    weight         double precision,
    iso_7250       boolean,
    tailoring      boolean
);

create sequence id_sequence
    maxvalue 1000;