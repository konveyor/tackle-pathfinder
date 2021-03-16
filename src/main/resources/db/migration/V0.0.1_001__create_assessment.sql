-- Hibernate Sequence
CREATE SEQUENCE hibernate_sequence START 1 INCREMENT 1;

-- Types definition
CREATE domain code_type AS varchar(255);
CREATE domain name_type AS VARCHAR(255);
CREATE domain user_type AS VARCHAR(255);
CREATE domain big_text_type AS VARCHAR(255);
CREATE domain id_type AS int8 NOT NULL;
CREATE domain order_type AS int8 NOT NULL;
CREATE domain language_type AS code_type NULL default 'EN';

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
    deleted boolean DEFAULT false,
    updateTime TIMESTAMP,
    updateUser user_type
);

-- TABLEs
CREATE TABLE assessment (
    LIKE audited_table_template INCLUDING ALL,

    status code_type NULL,
    application_id id_type,
    notes big_text_type NULL
);
