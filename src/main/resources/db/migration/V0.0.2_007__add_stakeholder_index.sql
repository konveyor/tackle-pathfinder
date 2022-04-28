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

-- Only unique stakeholders allowed per assessment
CREATE UNIQUE INDEX assesment_stakeholder_unique_idx
ON assessment_stakeholder (assessment_id, stakeholder_id)
WHERE (deleted is not true);

-- Only unique stakeholders allowed per assessment
CREATE UNIQUE INDEX assesment_stakeholdergroup_unique_idx
ON assessment_stakeholdergroup (assessment_id, stakeholdergroup_id)
WHERE (deleted is not true);
