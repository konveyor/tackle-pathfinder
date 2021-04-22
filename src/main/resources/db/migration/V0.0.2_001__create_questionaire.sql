create table questionnaire (
   id int8 not null,
   createTime timestamp,
   createUser varchar(255),
   deleted boolean not null default false,
   updateTime timestamp,
   updateUser varchar(255),
   language_code varchar(255) not null,
   name varchar(255) not null,
   primary key (id)
);
create table category (
   id int8 not null,
   createTime timestamp,
   createUser varchar(255),
   deleted boolean not null default false,
   updateTime timestamp,
   updateUser varchar(255),
   name varchar(255) not null,
   category_order int4 not null,
   questionnaire_id int8 not null,
   primary key (id)
);
create table question (
   id int8 not null,
   createTime timestamp,
   createUser varchar(255),
   deleted boolean not null default false,
   updateTime timestamp,
   updateUser varchar(255),
   name varchar(255) not null,
   question_order int4 not null,
   question_text varchar(500) not null,
   description varchar(1000),
   type varchar(255) not null,
   category_id int8,
   primary key (id)
);
create table single_option (
   id int8 not null,
   createTime timestamp,
   createUser varchar(255),
   deleted boolean not null default false,
   updateTime timestamp,
   updateUser varchar(255),
   option varchar(500) not null,
   singleoption_order int4 not null,
   risk varchar(255) not null,
   question_id int8 not null,
   primary key (id)
);
alter table if exists category
add constraint FKjqvqo75p8dpn6oweuepac4ubf foreign key (questionnaire_id) references questionnaire;
alter table if exists question
add constraint FK7jaqbm9p4prg7n91dd1uabrvj foreign key (category_id) references category;
alter table if exists single_option
add constraint FKi8whcw3jwcfm8sklck50a6g0e foreign key (question_id) references question;
