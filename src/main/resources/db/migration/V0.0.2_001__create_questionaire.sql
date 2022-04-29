--
-- Copyright © 2021 the Konveyor Contributors (https://konveyor.io/)
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

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
