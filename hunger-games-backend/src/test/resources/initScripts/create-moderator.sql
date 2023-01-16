INSERT INTO users (user_id, name, username, password)
VALUES ('76f1316a-3eec-46c6-a323-bf3060a3c72a', 'moderator-name',
        'moderator-username', '$2a$10$3X8Ncrru4KO6Kdyib8oP4.1cgrSM4oNR3Y8kzbCnb9teQGvoGLIUa');
INSERT INTO user_roles(user_id, role)
VALUES ('76f1316a-3eec-46c6-a323-bf3060a3c72a', 'MODERATOR');
INSERT INTO moderator (user_id)
VALUES ('76f1316a-3eec-46c6-a323-bf3060a3c72a');
