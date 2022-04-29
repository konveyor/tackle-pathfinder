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

UPDATE assessment_category
SET questionnaire_categoryid = category.id
FROM category
WHERE questionnaire_categoryid is null and assessment_category.name = category.name;

UPDATE assessment_question
SET questionnaire_questionid = subquery.id
FROM (select q.id, q.question_text, c.name
        from question q join category c on q.category_id = c.id) as subquery
where questionnaire_questionid is null and
      assessment_question.question_text = subquery.question_text and
      subquery.name = (select name from assessment_category where id = assessment_question.assessment_category_id);

UPDATE assessment_singleoption
SET questionnaire_optionid = subquery.id
FROM (select o.id, o.option, q.question_text from single_option o join question q on q.id = o.question_id) as subquery
where questionnaire_optionid is null and
        assessment_singleoption.option = subquery.option and
        subquery.question_text = (select question_text from assessment_question where id = assessment_singleoption.assessment_question_id);
