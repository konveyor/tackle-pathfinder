create table translated_text (
     id int8 not null,
     createTime timestamp,
     createUser varchar(255),
     deleted boolean,
     updateTime timestamp,
     updateUser varchar(255),
     key varchar(255),
     language varchar(255),
     text varchar(1000),
     primary key (id)
);
CREATE UNIQUE INDEX translated_text_key_language_unique_idx
    ON translated_text (key, language)
    WHERE (deleted is not true);

ALTER TABLE assessment_category ADD COLUMN questionnaire_categoryId int8;
ALTER TABLE assessment_question ADD COLUMN questionnaire_questionId int8;
ALTER TABLE assessment_singleoption ADD COLUMN questionnaire_optionId int8;