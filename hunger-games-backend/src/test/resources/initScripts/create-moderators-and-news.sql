INSERT INTO users (user_id, name, username, password) VALUES ('76f1316a-3eec-46c6-a323-bf3060a3c72a', '76f1316a-3eec-46c6-a323-bf3060a3c72a', '76f1316a-3eec-46c6-a323-bf3060a3c72a', 'hash');
INSERT INTO user_roles(user_id, role) VALUES ('76f1316a-3eec-46c6-a323-bf3060a3c72a', 'MODERATOR');
INSERT INTO moderator (user_id) values  ('76f1316a-3eec-46c6-a323-bf3060a3c72a');
INSERT INTO users (user_id, name, username, password) VALUES ('55d084b4-86f0-4c37-92b1-eba177d4cb44', '55d084b4-86f0-4c37-92b1-eba177d4cb44', '55d084b4-86f0-4c37-92b1-eba177d4cb44', 'hash');
INSERT INTO user_roles(user_id, role) VALUES ('55d084b4-86f0-4c37-92b1-eba177d4cb44', 'MODERATOR');
INSERT INTO moderator (user_id) values  ('55d084b4-86f0-4c37-92b1-eba177d4cb44');

insert into news(news_id, content, date_time, name, moderator_user_id) VALUES ('6978502a-59d4-49f9-967a-fa43249f7e92','content',CURRENT_TIMESTAMP,'name1','76f1316a-3eec-46c6-a323-bf3060a3c72a');
insert into news(news_id, content, date_time, name, moderator_user_id) VALUES ('3d53a976-a4bc-46fd-812e-4d315554769e','content',CURRENT_TIMESTAMP,'name2','76f1316a-3eec-46c6-a323-bf3060a3c72a');
insert into news(news_id, content, date_time, name, moderator_user_id) VALUES ('d52b2d63-894d-4ef6-89b1-5fc66e5a8992','content',CURRENT_TIMESTAMP,'name3','55d084b4-86f0-4c37-92b1-eba177d4cb44');
insert into news(news_id, content, date_time, name, moderator_user_id) VALUES ('8e975eb1-6ca1-4b1b-9291-f333cb39bd16','content',CURRENT_TIMESTAMP,'name4','55d084b4-86f0-4c37-92b1-eba177d4cb44');