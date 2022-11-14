--Sponsors:


--Mentors:
INSERT INTO users (name, username) VALUES ('mentor-name-1', 'mentor-1');
INSERT INTO users (name, username) VALUES ('mentor-name-2', 'mentor-2');
INSERT INTO user_roles(user_id, role) VALUES ('1', 'MENTOR');
INSERT INTO user_roles(user_id, role) VALUES ('2', 'MENTOR');
INSERT INTO mentor (user_id, district) values  ('1','1');
INSERT INTO mentor (user_id, district) values  ('2','3');
--Tributes:
INSERT INTO users (name, username) VALUES ('tribute-name-1', 'tribute-1');
INSERT INTO users (name, username) VALUES ('tribute-name-2', 'tribute-2');
INSERT INTO user_roles(user_id, role) VALUES ('3', 'TRIBUTE');
INSERT INTO user_roles(user_id, role) VALUES ('4', 'TRIBUTE');
INSERT INTO tribute (user_id,mentor_user_id) values  ('3','1');
INSERT INTO tribute (user_id,mentor_user_id) values  ('4','2');


--Resources:
INSERT INTO resource(id, name, price)
VALUES (1, 'Resource-1', 50);

INSERT INTO resource(id, name, price)
VALUES (2, 'Resource-2', 100);

INSERT INTO resource(id, name, price)
VALUES (3, 'Resource-3', 150);

INSERT INTO resource(id, name, price)
VALUES (4, 'Resource-4', 200);

INSERT INTO resource(id, name, price)
VALUES (5, 'Resource-5', 300);