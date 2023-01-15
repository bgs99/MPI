INSERT INTO users (user_id, name, username)
VALUES ('477b3f79-8d54-4e6e-90be-122a31d26c82', 'mentor', 'mentor');
INSERT INTO user_roles(user_id, role)
VALUES ('477b3f79-8d54-4e6e-90be-122a31d26c82', 'MENTOR');
INSERT INTO mentors (district, user_id)
values ('1', '477b3f79-8d54-4e6e-90be-122a31d26c82');

INSERT INTO users (user_id, name, username)
VALUES ('9667900f-24b2-4795-ad20-28b933d9ae32', 'tribute-test', 'tribute-name');
INSERT INTO user_roles(user_id, role)
VALUES ('9667900f-24b2-4795-ad20-28b933d9ae32', 'TRIBUTE');
INSERT INTO tributes (user_id, mentor_user_id)
values ('9667900f-24b2-4795-ad20-28b933d9ae32', '477b3f79-8d54-4e6e-90be-122a31d26c82');
