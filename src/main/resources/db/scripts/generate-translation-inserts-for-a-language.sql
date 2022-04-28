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

-- Categories
select 'INSERT INTO translated_text (id, deleted, key, text, language)
SELECT nextval(''hibernate_sequence''), false,
''Category_'' || id || ''_name'', ''name translation in XXXXX'', ''ES''
FROM category
WHERE name = ''' || name || ''' and deleted is not true;'
from category where deleted is not true;

-- Questions
select 'INSERT INTO translated_text (id, deleted, key, text, language)
select nextval(''hibernate_sequence''), false,
''Question_'' || question.id || ''_question'', ''question.question_text translation in XXXX'', ''ES''
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.question_text=''' || question.question_text || ''' and
       c.name = ''' || c.name || ''';'
FROM question join category c on question.category_id = c.id
where question.deleted is not true;

-- Question's description
select 'INSERT INTO translated_text (id, deleted, key, text, language)
select nextval(''hibernate_sequence''), false,
''Question_'' || question.id || ''_description'', ''question.description translation in language XXXX'', ''ES''
FROM question join category c on question.category_id = c.id
where question.deleted is not true and
       question.description=''' || question.description || ''' and
       c.name = ''' || c.name || ''';'
FROM question join category c on question.category_id = c.id
where question.deleted is not true;

-- Question's answer options
select 'INSERT INTO translated_text (id, deleted, key, text, language)
select nextval(''hibernate_sequence''), false,
''SingleOption_' || single_option.id || '_option'', ''single_option.option translation in language XXXX'', ''ES''
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true
       and single_option.option = ''' || option || '''
       and question.question_text = ''' || question.question_text || '''
       and c.name = ''' || c.name || ''';'
FROM single_option  join question on (question.id = single_option.question_id)
                    join category c on question.category_id = c.id
where single_option.deleted is not true;

