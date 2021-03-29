-- TABLEs
CREATE TABLE assessment (
    LIKE audited_table_template INCLUDING ALL,

    status code_type NULL,
    application_id id_type,
    notes big_text_type NULL
);
