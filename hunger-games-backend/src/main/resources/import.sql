--Mentors:
INSERT INTO users (user_id, name, username) VALUES ('d7f8e213-8d00-417c-89b3-94eea56b152b', 'mentor-name-1', 'mentor-1');
INSERT INTO users (user_id, name, username) VALUES ('2a20a33a-bca2-4a18-92a4-625784637a85', 'mentor-name-2', 'mentor-2');
INSERT INTO user_roles(user_id, role) VALUES ('d7f8e213-8d00-417c-89b3-94eea56b152b', 'MENTOR');
INSERT INTO user_roles(user_id, role) VALUES ('2a20a33a-bca2-4a18-92a4-625784637a85', 'MENTOR');
INSERT INTO mentor (user_id, district) values  ('d7f8e213-8d00-417c-89b3-94eea56b152b','1');
INSERT INTO mentor (user_id, district) values  ('2a20a33a-bca2-4a18-92a4-625784637a85','2');
--Tributes:
INSERT INTO users (user_id, name, username) VALUES ('effa6ed2-1457-4bdd-a53d-c7b73137f917', 'tribute-name-1', 'tribute-1');
INSERT INTO users (user_id, name, username) VALUES ('9539c9f8-2771-45ed-a8e2-b7a1c353f5bd', 'tribute-name-2', 'tribute-2');
INSERT INTO user_roles(user_id, role) VALUES ('effa6ed2-1457-4bdd-a53d-c7b73137f917', 'TRIBUTE');
INSERT INTO user_roles(user_id, role) VALUES ('9539c9f8-2771-45ed-a8e2-b7a1c353f5bd', 'TRIBUTE');
INSERT INTO tribute (user_id,mentor_user_id) values  ('effa6ed2-1457-4bdd-a53d-c7b73137f917','d7f8e213-8d00-417c-89b3-94eea56b152b');
INSERT INTO tribute (user_id,mentor_user_id) values  ('9539c9f8-2771-45ed-a8e2-b7a1c353f5bd','2a20a33a-bca2-4a18-92a4-625784637a85');
--Resources:
INSERT INTO resource(resource_id, name, price) VALUES ('a7ea0f48-eadb-46e7-9642-13998c6613cf', 'Resource-1', 50);
INSERT INTO resource(resource_id, name, price) VALUES ('dc43d8f5-7b08-48d5-a082-2286ef5e345d', 'Resource-2', 100);
INSERT INTO resource(resource_id, name, price) VALUES ('19a3ab8f-ef5f-4f8a-bb8e-eb28d45ce9ac', 'Resource-3', 150);
INSERT INTO resource(resource_id, name, price) VALUES ('ce371850-aa6d-4e42-9097-0cec8cc6769e', 'Resource-4', 200);
INSERT INTO resource(resource_id, name, price) VALUES ('90734358-23e8-4d0c-be7b-3918ef6157b5', 'Resource-5', 300);