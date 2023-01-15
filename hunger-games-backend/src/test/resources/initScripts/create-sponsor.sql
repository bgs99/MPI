INSERT INTO users (user_id, name, username, password) VALUES ('4a9f1d37-c6fd-4391-8082-655bb98fb460', 'sponsor-test', 'sponsor-name', '$2a$10$3X8Ncrru4KO6Kdyib8oP4.1cgrSM4oNR3Y8kzbCnb9teQGvoGLIUa');
INSERT INTO user_roles(user_id, role) VALUES ('4a9f1d37-c6fd-4391-8082-655bb98fb460', 'SPONSOR');
INSERT INTO sponsors (user_id) values  ('4a9f1d37-c6fd-4391-8082-655bb98fb460');