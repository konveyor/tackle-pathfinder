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
