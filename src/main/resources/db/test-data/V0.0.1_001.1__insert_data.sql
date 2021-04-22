INSERT INTO assessment (id, status, application_id, comment, createuser, createtime) 
values 
(nextval('hibernate_sequence'), 'STARTED', 20, 'This is a simple note\nAnd a new line.', 'testuser', CURRENT_TIMESTAMP);