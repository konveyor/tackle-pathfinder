--
-- Copyright © 2021 Konveyor (https://konveyor.io/)
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

-- Hibernate Sequence
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1 INCREMENT 1;
-- TABLEs
create table assessment (
    id int8 not null,
    createTime timestamp,
    createUser varchar(255),
    deleted boolean not null default false,
    updateTime timestamp,
    updateUser varchar(255),
    application_id int8 not null,
    comment varchar(1000),
    status varchar(255),
    primary key (id)
);
