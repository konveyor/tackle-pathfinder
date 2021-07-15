-- This script inserts a translation of all the elements in 2 additional languages ES, IT
-- simply creates the translation prefixing the text with the language code
INSERT INTO translated_text
(id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false, 'Category_' || id || '_name', 'ES: ' || name, 'ES'
FROM category
where deleted is not true;

INSERT INTO translated_text
(id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false, 'Category_' || id || '_name', 'IT: ' || name, 'IT'
FROM category
where deleted is not true;

INSERT INTO translated_text
(id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false, 'Question_' || id || '_question', 'ES: ' || question.question_text, 'ES'
FROM question
where deleted is not true;

INSERT INTO translated_text
(id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false, 'Question_' || id || '_question', 'IT: ' || question.question_text, 'IT'
FROM question
where deleted is not true;

INSERT INTO translated_text
(id, deleted, key, text, language)
SELECT nextval('hibernate_sequence'), false, 'Question_' || id || '_description', 'ES: ' || question.description, 'ES'
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
SELECT nextval('hibernate_sequence'), false, 'SingleOption_' || id || '_option', 'ES: ' || option, 'ES'
FROM single_option
where deleted is not true;

--delete from translated_text;
