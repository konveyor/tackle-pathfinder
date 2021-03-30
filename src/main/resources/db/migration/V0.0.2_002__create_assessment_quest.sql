    create table assessment_category (
       id int8 not null,
        createTime timestamp,
        createUser varchar(255),
        deleted boolean,
        updateTime timestamp,
        updateUser varchar(255),
        name varchar(255),
        category_order int4,
        questionnaireId int8,
        primary key (id)
    );

    create table assessment_question (
       id int8 not null,
        createTime timestamp,
        createUser varchar(255),
        deleted boolean,
        updateTime timestamp,
        updateUser varchar(255),
        comment varchar(255),
        name varchar(255),
        question_order int4,
        text varchar(255),
        tooltip varchar(255),
        type varchar(255),
        category_id int8,
        primary key (id)
    );

    create table assessment_questionnaire (
       id int8 not null,
        createTime timestamp,
        createUser varchar(255),
        deleted boolean,
        updateTime timestamp,
        updateUser varchar(255),
        language_code varchar(255) not null,
        name varchar(255),
        assessmentId int8,
        questionnaireId int8,
        primary key (id)
    );

    create table assessment_singleoption (
       id int8 not null,
        createTime timestamp,
        createUser varchar(255),
        deleted boolean,
        updateTime timestamp,
        updateUser varchar(255),
        option varchar(255),
        singleoption_order int4,
        risk int4,
        questionId int8 not null,
        primary key (id)
    );

    alter table if exists assessment_singleoption 
       add constraint FK7yuubk41hybcojhk3uin94bj1 
       foreign key (questionId) 
       references assessment_question;

    alter table if exists assessment_questionnaire 
       add constraint FKs6mwnhrlyg8ae32ei95aigrmk 
       foreign key (questionnaireId) 
       references questionnaire;

    alter table if exists assessment_questionnaire 
       add constraint FK6pfuk1trqx58qfrr0e0nm77ms 
       foreign key (assessmentId) 
       references assessment;

    alter table if exists assessment_question 
       add constraint FK7ya3lv02iu8jwfx0m2mo2mwfx 
       foreign key (category_id) 
       references assessment_category;

    alter table if exists assessment_category 
       add constraint FKg7m8p00b2cw73bj67jh49ijq0 
       foreign key (questionnaireId) 
       references assessment_questionnaire;
