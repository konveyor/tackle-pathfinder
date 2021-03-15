INSERT INTO assessment (id, name, status, application_id, notes, createuser, createtime, owner_id) 
values 
(nextval('hibernate_sequence'), 'test name', 'STARTED', 20, 'This is a simple note\nAnd a new line.', 'testuser', CURRENT_TIMESTAMP, 1000 );