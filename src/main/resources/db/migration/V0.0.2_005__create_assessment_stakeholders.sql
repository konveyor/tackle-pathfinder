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