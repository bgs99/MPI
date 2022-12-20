INSERT INTO users (user_id, name, username) VALUES ('1d3ad419-e98f-43f1-9ac6-08776036cded', 'mentor-test', '1d3ad419-e98f-43f1-9ac6-08776036cded');
INSERT INTO user_roles(user_id, role) VALUES ('1d3ad419-e98f-43f1-9ac6-08776036cded', 'MENTOR');
INSERT INTO mentors (user_id, district) values  ('1d3ad419-e98f-43f1-9ac6-08776036cded', '1');

INSERT INTO users (user_id, name, username) VALUES ('9667900f-24b2-4795-ad20-28b933d9ae32', 'tribute-test1', '9667900f-24b2-4795-ad20-28b933d9ae32');
INSERT INTO user_roles(user_id, role) VALUES ('9667900f-24b2-4795-ad20-28b933d9ae32', 'TRIBUTE');
INSERT INTO tributes (user_id, mentor_user_id) values  ('9667900f-24b2-4795-ad20-28b933d9ae32', '1d3ad419-e98f-43f1-9ac6-08776036cded');

INSERT INTO users (user_id, name, username) VALUES ('c0b91cca-27ba-49d2-85e1-290cbd73d45e', 'tribute-test2', 'c0b91cca-27ba-49d2-85e1-290cbd73d45e');
INSERT INTO user_roles(user_id, role) VALUES ('c0b91cca-27ba-49d2-85e1-290cbd73d45e', 'TRIBUTE');
INSERT INTO tributes (user_id, mentor_user_id) values  ('c0b91cca-27ba-49d2-85e1-290cbd73d45e', '1d3ad419-e98f-43f1-9ac6-08776036cded');

INSERT INTO users (user_id, name, username) VALUES ('3ee25464-bdec-4237-a335-94c1f376e8d6', 'tribute-test3', '3ee25464-bdec-4237-a335-94c1f376e8d6');
INSERT INTO user_roles(user_id, role) VALUES ('3ee25464-bdec-4237-a335-94c1f376e8d6', 'TRIBUTE');
INSERT INTO tributes (user_id, mentor_user_id) values  ('3ee25464-bdec-4237-a335-94c1f376e8d6', '1d3ad419-e98f-43f1-9ac6-08776036cded');