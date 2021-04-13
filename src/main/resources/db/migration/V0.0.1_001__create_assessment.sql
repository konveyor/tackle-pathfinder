-- Hibernate Sequence
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1 INCREMENT 1;
-- TABLEs
create table assessment (
    id int8 not null,
    createTime timestamp,
    createUser varchar(255),
    deleted boolean not null default false,
    updateTime timestamp,
    updateUser varchar(255),
    application_id int8 not null,
    comment varchar(1000),
    status varchar(255),
    primary key (id)
);
