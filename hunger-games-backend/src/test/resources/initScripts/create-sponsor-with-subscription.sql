INSERT INTO users (user_id, name, username, password) VALUES ('4a9f1d37-c6fd-4391-8082-655bb98fb460', 'sponsor-test', 'sponsor-name', 'hash');
INSERT INTO user_roles(user_id, role) VALUES ('4a9f1d37-c6fd-4391-8082-655bb98fb460', 'SPONSOR');
INSERT INTO sponsors (user_id) values  ('4a9f1d37-c6fd-4391-8082-655bb98fb460');

INSERT INTO orders(order_id, approved, paid, price) VALUES ('9e33833d-34d2-46c6-8354-d8f6e05f4410', true, true, 100);
INSERT INTO news_subscription_orders(email, order_id, sponsor_user_id) values ('example@email.com', '9e33833d-34d2-46c6-8354-d8f6e05f4410', '4a9f1d37-c6fd-4391-8082-655bb98fb460');

UPDATE sponsors set news_subscription_order_order_id='9e33833d-34d2-46c6-8354-d8f6e05f4410' where user_id='4a9f1d37-c6fd-4391-8082-655bb98fb460';