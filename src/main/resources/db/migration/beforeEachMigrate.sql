-- Hibernate Sequence
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1 INCREMENT 1 ;

-- Types definition
DO $$
BEGIN

IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'code_type') THEN
  CREATE domain code_type AS varchar(255);
END IF;

IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'name_type') THEN
  CREATE domain name_type AS VARCHAR(255);
END IF;

IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'user_type') THEN
  CREATE domain user_type AS VARCHAR(255);
END IF;

IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'big_text_type') THEN
  CREATE domain big_text_type AS VARCHAR(255);
END IF;

IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'id_type') THEN
  CREATE domain id_type AS int8 NOT NULL;
END IF;

IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'order_type') THEN
  CREATE domain order_type AS int8 NOT NULL;
END IF;

IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'language_type') THEN
  CREATE domain language_type AS code_type NULL default 'EN';
END IF;
END
$$;
-- TABLE audited_table_template
CREATE TEMPORARY TABLE IF NOT EXISTS simple_table_template (
    id id_type NOT NULL,
    PRIMARY KEY (id)
);

CREATE TEMPORARY TABLE  IF NOT EXISTS audited_table_template (
    LIKE simple_table_template INCLUDING ALL,

    createUser user_type NOT NULL,
    createTime timestamp NOT NULL,
    deleted boolean DEFAULT false,
    updateTime TIMESTAMP NULL,
    updateUser user_type
);