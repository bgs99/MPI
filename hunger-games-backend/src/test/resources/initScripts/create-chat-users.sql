INSERT INTO users (user_id, name, username) VALUES ('4a9f1d37-c6fd-4391-8082-655bb98fb460', 'sponsor-test', 'sponsor-name');
INSERT INTO user_roles(user_id, role) VALUES ('4a9f1d37-c6fd-4391-8082-655bb98fb460', 'SPONSOR');
INSERT INTO sponsors (user_id) values  ('4a9f1d37-c6fd-4391-8082-655bb98fb460');

INSERT INTO users (user_id, name, username) VALUES ('1d3ad419-e98f-43f1-9ac6-08776036cded', 'mentor-test', 'mentor-name');
INSERT INTO user_roles(user_id, role) VALUES ('1d3ad419-e98f-43f1-9ac6-08776036cded', 'MENTOR');
INSERT INTO mentors (user_id, district) values  ('1d3ad419-e98f-43f1-9ac6-08776036cded', '1');

INSERT INTO users (user_id, name, username) VALUES ('9667900f-24b2-4795-ad20-28b933d9ae32', 'tribute-test1', 'tribute-name1');
INSERT INTO user_roles(user_id, role) VALUES ('9667900f-24b2-4795-ad20-28b933d9ae32', 'TRIBUTE');
INSERT INTO tributes (user_id, mentor_user_id) values  ('9667900f-24b2-4795-ad20-28b933d9ae32', '1d3ad419-e98f-43f1-9ac6-08776036cded');
