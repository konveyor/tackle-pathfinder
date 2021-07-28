create table assessment_bulk (
    id int8 not null,
    createTime timestamp,
    createUser varchar(255),
    deleted boolean,
    updateTime timestamp,
    updateUser varchar(255),
    applications varchar(255) not null,
    completed boolean default false not null,
    fromAssessmentId int8,
    primary key (id)
);
create table assessment_bulk_app (
    id int8 not null,
    createTime timestamp,
    createUser varchar(255),
    deleted boolean,
    updateTime timestamp,
    updateUser varchar(255),
    applicationId int8 not null,
    assessmentId int8,
    error varchar(255),
    assessment_bulk_id int8,
    primary key (id)
);
alter table if exists assessment_bulk_app
add constraint FKle93oc7fe96n7tg1w5domasn2 
foreign key (assessment_bulk_id) references assessment_bulk;