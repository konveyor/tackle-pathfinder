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

