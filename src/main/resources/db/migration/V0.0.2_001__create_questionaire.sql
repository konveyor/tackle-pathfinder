    create table category (
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
    create table question (
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
    create table questionnaire (
       id int8 not null,
        createTime timestamp,
        createUser varchar(255),
        deleted boolean,
        updateTime timestamp,
        updateUser varchar(255),
        language_code varchar(255) not null,
        name varchar(255),
        primary key (id)
    );
    create table single_option (
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
    alter table if exists category 
       add constraint FKjqvqo75p8dpn6oweuepac4ubf 
       foreign key (questionnaireId) 
       references questionnaire;

    alter table if exists question 
       add constraint FK7jaqbm9p4prg7n91dd1uabrvj 
       foreign key (category_id) 
       references category;

    alter table if exists single_option 
       add constraint FKi8whcw3jwcfm8sklck50a6g0e 
       foreign key (questionId) 
       references question;
