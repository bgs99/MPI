INSERT INTO settings(setting_id, email) VALUES ('934986c1-e747-464f-822f-b2f6e6da1b3d', 'test1@email.com');
INSERT INTO settings(setting_id, email) VALUES ('d8d7b240-67c6-4f5d-a76e-8a99183711cc', 'test2@email.com');
INSERT INTO settings(setting_id, email) VALUES ('ffafd46b-da88-40fe-acf6-5bb39ff2dd3c', 'test3@email.com');

INSERT INTO users (user_id, name, username, setting_id) VALUES ('4a9f1d37-c6fd-4391-8082-655bb98fb460', 'sponsor-test1', 'sponsor-name1','934986c1-e747-464f-822f-b2f6e6da1b3d');
INSERT INTO user_roles(user_id, role) VALUES ('4a9f1d37-c6fd-4391-8082-655bb98fb460', 'SPONSOR');
INSERT INTO sponsors (user_id) values  ('4a9f1d37-c6fd-4391-8082-655bb98fb460');

INSERT INTO users (user_id, name, username, setting_id) VALUES ('e37ec309-5b49-4985-97c0-e12451dc177e', 'sponsor-test2', 'sponsor-name2','d8d7b240-67c6-4f5d-a76e-8a99183711cc');
INSERT INTO user_roles(user_id, role) VALUES ('e37ec309-5b49-4985-97c0-e12451dc177e', 'SPONSOR');
INSERT INTO sponsors (user_id) values  ('e37ec309-5b49-4985-97c0-e12451dc177e');

INSERT INTO users (user_id, name, username, setting_id) VALUES ('1ebfbc1c-fde8-40fa-8572-488738c16a9b', 'sponsor-test3', 'sponsor-name3','ffafd46b-da88-40fe-acf6-5bb39ff2dd3c');
INSERT INTO user_roles(user_id, role) VALUES ('1ebfbc1c-fde8-40fa-8572-488738c16a9b', 'SPONSOR');
INSERT INTO sponsors (user_id) values  ('1ebfbc1c-fde8-40fa-8572-488738c16a9b');
