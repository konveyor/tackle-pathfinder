create table assessment_stakeholder (
   id int8 not null,
   createTime timestamp,
   createUser varchar(255),
   deleted boolean not null default false,
   updateTime timestamp,
   updateUser varchar(255),
   stakeholder_id int8,
   assessment_id int8,
   primary key (id)
);
create table assessment_stakeholdergroup (
   id int8 not null,
   createTime timestamp,
   createUser varchar(255),
   deleted boolean not null default false,
   updateTime timestamp,
   updateUser varchar(255),
   stakeholdergroup_id int8,
   assessment_id int8,
   primary key (id)
);
alter table if exists assessment_stakeholder
add constraint assessment_stakeholder_to_assessment_FK foreign key (assessment_id) references assessment;
alter table if exists assessment_stakeholdergroup
add constraint assessment_stakeholdergroup_to_assessment_FK foreign key (assessment_id) references assessment;