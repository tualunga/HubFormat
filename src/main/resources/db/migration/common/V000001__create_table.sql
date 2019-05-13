create table hbdf_table
(
    id             serial not null
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