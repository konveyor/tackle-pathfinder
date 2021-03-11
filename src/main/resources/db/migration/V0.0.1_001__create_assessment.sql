-- Hibernate Sequence
CREATE SEQUENCE hibernate_sequence START 1 INCREMENT 1;

-- Types definition
CREATE TYPE language_enum AS ENUM ('ES', 'EN', 'FR');
CREATE TYPE question_enum AS ENUM ('SINGLE', 'MULTI');
CREATE TYPE risk_enum AS ENUM ('RED','AMBER','GREEN');
CREATE TYPE status_enum AS ENUM ('STARTED', 'COMPLETE', 'DISCARDED');

CREATE domain name_type AS VARCHAR(255);
CREATE domain user_type AS VARCHAR(255);
CREATE domain big_text_type AS VARCHAR(255);
CREATE domain id_type AS int8 NOT NULL;
CREATE domain order_type AS int8 NOT NULL;
CREATE domain language_type AS language_enum NOT NULL default 'EN';

-- TABLE audited_table_template
CREATE TEMPORARY TABLE simple_table_template (
    id id_type NOT NULL,
    name name_type NOT NULL,
    PRIMARY KEY (id)
);

CREATE TEMPORARY TABLE audited_table_template (
    LIKE simple_table_template INCLUDING ALL,

    createUser user_type NOT NULL,
    createTime timestamp NOT NULL,
    owner_id id_type NOT NULL,
    deleted boolean,
    updateTime TIMESTAMP,
    updateUser user_type
);

-- TABLEs
CREATE TABLE assessment (
    LIKE audited_table_template INCLUDING ALL,

    assess_status status_enum NULL,
    application_id id_type,
    language language_type,
    application_id id_type,
    questionaire_id id_type
);
