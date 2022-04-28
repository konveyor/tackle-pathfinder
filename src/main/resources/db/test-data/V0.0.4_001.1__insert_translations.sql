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

-- This script inserts a translation of all the elements in 2 additional languages CA, IT
-- simply creates the translation prefixing the text with the language code
INSERT INTO translated_text
(id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false, 'Category_' || id || '_name', 'CA: ' || name, 'CA'
FROM category
where deleted is not true;

INSERT INTO translated_text
(id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false, 'Category_' || id || '_name', 'IT: ' || name, 'IT'
FROM category
where deleted is not true;

INSERT INTO translated_text
(id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false, 'Question_' || id || '_question', 'CA: ' || question.question_text, 'CA'
FROM question
where deleted is not true;

INSERT INTO translated_text
(id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false, 'Question_' || id || '_question', 'IT: ' || question.question_text, 'IT'
FROM question
where deleted is not true;

INSERT INTO translated_text
(id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false, 'Question_' || id || '_description', 'CA: ' || question.description, 'CA'
FROM question
where deleted is not true;

INSERT INTO translated_text
(id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false, 'Question_' || id || '_description', 'IT: ' || question.description, 'IT'
FROM question
where deleted is not true;

INSERT INTO translated_text
(id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false, 'SingleOption_' || id || '_option', 'IT: ' || option, 'IT'
FROM single_option
where deleted is not true;

INSERT INTO translated_text
(id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false, 'SingleOption_' || id || '_option', 'CA: ' || option, 'CA'
FROM single_option
where deleted is not true;

--delete from translated_text;
