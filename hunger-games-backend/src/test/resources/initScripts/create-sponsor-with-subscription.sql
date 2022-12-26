insert into settings(setting_id, email) VALUES ('3770eb1d-76c7-418c-b2fa-9948f381aac7', 'emailtest@capitol.com');

INSERT INTO users (user_id, name, username, password, setting_id) VALUES ('523adea8-9f98-41a3-bae0-1e4875aceaae', 'sponsor-test', 'sponsor-name', 'hash', '3770eb1d-76c7-418c-b2fa-9948f381aac7');
INSERT INTO user_roles(user_id, role) VALUES ('523adea8-9f98-41a3-bae0-1e4875aceaae', 'SPONSOR');
INSERT INTO sponsors (user_id) values  ('523adea8-9f98-41a3-bae0-1e4875aceaae');

INSERT INTO orders(order_id, approved, paid, price) VALUES ('9e33833d-34d2-46c6-8354-d8f6e05f4410', true, true, 100);
INSERT INTO news_subscription_orders(email, order_id, sponsor_user_id) values ('example@email.com', '9e33833d-34d2-46c6-8354-d8f6e05f4410', '523adea8-9f98-41a3-bae0-1e4875aceaae');

UPDATE sponsors set news_subscription_order_order_id='9e33833d-34d2-46c6-8354-d8f6e05f4410' where user_id='523adea8-9f98-41a3-bae0-1e4875aceaae';