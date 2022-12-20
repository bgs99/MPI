-----table users
INSERT INTO users (user_id, name, username) VALUES ('4a9f1d37-c6fd-4391-8082-655bb98fb460', 'sponsor-test', 'sponsor-name');
--check unique username
INSERT INTO users (user_id, name, username) VALUES ('4a9f1d32-c6fd-4391-8082-655bb98fb460', 'sponsor-test', 'sponsor-name');
--check avatar_uri
---INSERT INTO users (user_id, name, username,avatar_uri) VALUES ('1a9f1d37-c6fd-4391-8082-655bb98fb460', 'sponsor-test', 'sponsor-name1', 'https://www.wallpaperflare.com/static/642/294/549/cat-grass-leaves-autumn-wallpaper.jpg');
---INSERT INTO users (user_id, name, username,avatar_uri) VALUES ('2a9f1d37-c6fd-4391-8082-655bb98fb460', 'sponsor-test', 'sponsor-name2','http://www.wallpaperflare.com/static/642/294/549/cat-grass-leaves-autumn-wallpaper.jpg');
---INSERT INTO users (user_id, name, username,avatar_uri) VALUES ('3a9f1d37-c6fd-4391-8082-655bb98fb460', 'sponsor-test', 'sponsor-name3', 'vsem privet');

--empty name
INSERT INTO users (user_id, name, username) VALUES ('fbc4b112-73fa-4eb5-bc51-df9690324302', '', 'fbc4b112-73fa-4eb5-bc51-df9690324302');

INSERT INTO users (user_id, name, username) VALUES ('129f1d37-c6fd-4391-8082-655bb98fb460', 'sponsor-test', '');



-----table user_roles
INSERT INTO users (user_id, name, username) VALUES ('fad54d5c-89e0-4240-9fda-9e2835579c1d', 'sponsor-test', 'sponsor-name-us');
INSERT INTO user_roles(user_id, role) VALUES ('fad54d5c-89e0-4240-9fda-9e2835579c1d','SPONSOR');
--check enum
---INSERT INTO user_roles(user_id, role) VALUES ('fad54d5c-89e0-4240-9fda-9e2835579c1d','TEST');



-----table sponsors
INSERT INTO users (user_id, name, username) VALUES ('83137742-b52e-4689-a9fc-714461f49e69', 'sponsor-test', 'sponsor-name31');
INSERT INTO sponsors (user_id) values  ('83137742-b52e-4689-a9fc-714461f49e69');
--check user id fk
INSERT INTO sponsors (user_id) values  ('532dc7b6-e826-422e-a090-c664fb8da628');



-----table moderators
INSERT INTO users (user_id, name, username) VALUES ('359cc972-e981-4e13-862f-f7bc34276b88', 'sponsor-test', '359cc972-e981-4e13-862f-f7bc34276b88');
INSERT INTO sponsors (user_id) values  ('359cc972-e981-4e13-862f-f7bc34276b88');
--check user id fk
INSERT INTO sponsors (user_id) values  ('532dc7b6-e826-422e-a090-c664fb8da628');



-----table mentors
INSERT INTO users (user_id, name, username) VALUES ('6636d82a-4014-4b2b-9601-abfad546eb3c', 'mentor-test', 'mentor-test-name');
insert into mentors(district, user_id) VALUES (1,'6636d82a-4014-4b2b-9601-abfad546eb3c');
--check district size
INSERT INTO users (user_id, name, username) VALUES ('c046d6ee-c944-497c-9b0a-b69f8eab74b2', 'mentor-test', 'mentor-test-name-1');
---insert into mentors(district, user_id) VALUES (500,'c046d6ee-c944-497c-9b0a-b69f8eab74b2');
--check user id fk
INSERT INTO sponsors (user_id) values  ('532dc7b6-e826-422e-a090-c664fb8da628');



--table tributes
INSERT INTO users (user_id, name, username) VALUES ('3bb2b5fe-e716-4a23-baae-68cd6c7885b2', 'mentor-test', 'mentor-test-name-2');
insert into mentors(district, user_id) VALUES (1,'3bb2b5fe-e716-4a23-baae-68cd6c7885b2');
INSERT INTO users (user_id, name, username) VALUES ('0b74064c-b565-410e-b97c-53f10e0e29f9', 'tribute-test', 'tribute-test-name');
insert into tributes(user_id, mentor_user_id) VALUES ('0b74064c-b565-410e-b97c-53f10e0e29f9','3bb2b5fe-e716-4a23-baae-68cd6c7885b2');
--check user id fk
INSERT INTO sponsors (user_id) values  ('532dc7b6-e826-422e-a090-c664fb8da628');
--check mentor id fk
INSERT INTO users (user_id, name, username) VALUES ('6740f932-7665-455f-9c03-90f94e6c26dd', 'tribute-test', '6740f932-7665-455f-9c03-90f94e6c26dd');
insert into tributes(user_id, mentor_user_id) VALUES ('740f932-7665-455f-9c03-90f94e6c26dd','532dc7b6-e826-422e-a090-c664fb8da628');



-----table settings
insert into settings(setting_id, email) VALUES ('4eb12328-e5b2-491d-ae54-d0b5b19193af', 'examaple@mail.com');
--check email
---insert into settings(setting_id, email) VALUES ('fb7abb9d-7dca-472d-83b3-d396bb13b8ba', 'vsem privet');
--check one-to-one with users
---insert into settings(setting_id, email) VALUES ('0cd6d0cc-f9d6-4d03-831a-7b7f7c22303d', 'examaple@mail.com');
---INSERT INTO users (user_id, name, username, setting_id) VALUES ('b3edcfe3-b3ea-47ba-b66f-18e7fa23786c', 'tribute-test', 'b3edcfe3-b3ea-47ba-b66f-18e7fa23786c','0cd6d0cc-f9d6-4d03-831a-7b7f7c22303d');
---INSERT INTO users (user_id, name, username, setting_id) VALUES ('c50ae222-e133-4d4b-bd8c-3678163dbf26', 'tribute-test', 'c50ae222-e133-4d4b-bd8c-3678163dbf26','0cd6d0cc-f9d6-4d03-831a-7b7f7c22303d');



-----table resources
insert into resources(resource_id, name, price) VALUES ('8a0b785d-0c42-4254-a7d9-1af2002e2a2e','',1);
---check not null name
insert into resources(resource_id, price) VALUES ('bdbf114e-17fa-4a11-aaf6-9f9a8b8014df',1);



-----table orders
insert into orders(order_id, approved, paid, price) VALUES ('35fbc486-2adc-42c1-8392-a4e87895ef0d', null, true, 0);
--check price not null
insert into orders(order_id, approved, paid) VALUES ('75d84883-1908-4829-b19a-4dc205c94e53', null, true);



-----table order_details
INSERT INTO resources (resource_id, name, price) VALUES ('af37c3d0-85fc-43b8-b8a9-d05fca580c46', 'af37c3d0-85fc-43b8-b8a9-d05fca580c46', 50);
INSERT INTO resources (resource_id, name, price) VALUES ('5f1e5a10-31ba-4150-b6ee-56f688f3f79f', '5f1e5a10-31ba-4150-b6ee-56f688f3f79f', 100);
INSERT INTO resources (resource_id, name, price) VALUES ('e788285e-980c-4a42-9588-44b8897cd0a9', 'e788285e-980c-4a42-9588-44b8897cd0a9', 200);
insert into orders(order_id, approved, paid, price) VALUES ('0958f796-c2a7-44a9-9137-dc6a67707d69', null, true, 0);
INSERT INTO users (user_id, name, username) VALUES ('cf79239b-0950-4264-9d43-152cd3a63df1', 'test', 'cf79239b-0950-4264-9d43-152cd3a63df1');
INSERT INTO users (user_id, name, username) VALUES ('50caeea7-30ad-4263-8d0b-24b79426cf30', 'test', '50caeea7-30ad-4263-8d0b-24b79426cf30');
INSERT INTO sponsors (user_id) values  ('cf79239b-0950-4264-9d43-152cd3a63df1');
INSERT INTO tributes (user_id) values  ('50caeea7-30ad-4263-8d0b-24b79426cf30');
insert into resource_orders(order_id, sponsor_user_id, tribute_user_id) VALUES ('0958f796-c2a7-44a9-9137-dc6a67707d69','cf79239b-0950-4264-9d43-152cd3a63df1','50caeea7-30ad-4263-8d0b-24b79426cf30');
--check min size
insert into order_details(order_detail_id, size, resource_resource_id, order_id) values ('15e2436e-10f2-4bf6-b7a4-6e3fa1ead4be', 0,'5f1e5a10-31ba-4150-b6ee-56f688f3f79f','0958f796-c2a7-44a9-9137-dc6a67707d69');
--check no such resource and order
insert into order_details(order_detail_id, size, resource_resource_id, order_id) values ('15e2436e-10f2-4bf6-b7a4-6e3fa1ead4be', 1,'a329ff4d-e362-406b-9775-6be1d2a9c3fe','0958f796-c2a7-44a9-9137-dc6a67707d69');
insert into order_details(order_detail_id, size, resource_resource_id, order_id) values ('15e2436e-10f2-4bf6-b7a4-6e3fa1ead4be', 0,'5f1e5a10-31ba-4150-b6ee-56f688f3f79f','522acf24-4ae5-43a3-80ff-65f1c36660a5');
--valid insert
insert into order_details(order_detail_id, size, resource_resource_id, order_id) values ('15e2436e-10f2-4bf6-b7a4-6e3fa1ead4be', 1,'5f1e5a10-31ba-4150-b6ee-56f688f3f79f','0958f796-c2a7-44a9-9137-dc6a67707d69');



-----table resource_orders
insert into orders(order_id, approved, paid, price) VALUES ('12a029c3-d9be-4e88-b6e6-864490ec2086', null, true, 0);
INSERT INTO users (user_id, name, username) VALUES ('29e5cc59-9895-4f65-b95b-bc5832e4c188', 'test', '29e5cc59-9895-4f65-b95b-bc5832e4c188');
INSERT INTO users (user_id, name, username) VALUES ('e03a07e1-f26b-4cc7-9ae6-69d01e119fa9', 'test', 'e03a07e1-f26b-4cc7-9ae6-69d01e119fa9');
INSERT INTO sponsors (user_id) values  ('29e5cc59-9895-4f65-b95b-bc5832e4c188');
INSERT INTO tributes (user_id) values  ('e03a07e1-f26b-4cc7-9ae6-69d01e119fa9');
--no such order
insert into resource_orders(order_id, sponsor_user_id, tribute_user_id) VALUES ('12a9bd10-6442-43c7-a5be-4f12717d220a','29e5cc59-9895-4f65-b95b-bc5832e4c188','e03a07e1-f26b-4cc7-9ae6-69d01e119fa9');
--no such sponsor
insert into resource_orders(order_id, sponsor_user_id, tribute_user_id) VALUES ('12a029c3-d9be-4e88-b6e6-864490ec2086','12a9bd10-6442-43c7-a5be-4f12717d220a','e03a07e1-f26b-4cc7-9ae6-69d01e119fa9');
--no such tribute
insert into resource_orders(order_id, sponsor_user_id, tribute_user_id) VALUES ('12a029c3-d9be-4e88-b6e6-864490ec2086','29e5cc59-9895-4f65-b95b-bc5832e4c188','12a9bd10-6442-43c7-a5be-4f12717d220a');

--valid insert
insert into resource_orders(order_id, sponsor_user_id, tribute_user_id) VALUES ('12a029c3-d9be-4e88-b6e6-864490ec2086','29e5cc59-9895-4f65-b95b-bc5832e4c188','e03a07e1-f26b-4cc7-9ae6-69d01e119fa9');



-----table advertisement_orders
insert into orders(order_id, approved, paid, price) VALUES ('06d3d284-8d07-465f-b827-27c5cb31bb99', null, true, 0);
INSERT INTO users (user_id, name, username) VALUES ('f27b858f-9811-40e4-b78a-ab913e011142', 'test', 'f27b858f-9811-40e4-b78a-ab913e011142');
INSERT INTO tributes (user_id) values  ('f27b858f-9811-40e4-b78a-ab913e011142');
-- no such tribute
insert into advertisement_orders(advertising_text, order_id, tribute_user_id) values ('some text', '06d3d284-8d07-465f-b827-27c5cb31bb99','94706886-d53e-41ab-bd5b-82bce0ae9917');
--no such order
insert into advertisement_orders(advertising_text, order_id, tribute_user_id) values ('some text', '94706886-d53e-41ab-bd5b-82bce0ae9917','f27b858f-9811-40e4-b78a-ab913e011142');
--text is null
insert into advertisement_orders(order_id, tribute_user_id) values ('06d3d284-8d07-465f-b827-27c5cb31bb99','f27b858f-9811-40e4-b78a-ab913e011142');
--valid insert
insert into advertisement_orders(advertising_text, order_id, tribute_user_id) values ('some text', '06d3d284-8d07-465f-b827-27c5cb31bb99','f27b858f-9811-40e4-b78a-ab913e011142');



----table chats
INSERT INTO users (user_id, name, username) VALUES ('e3661968-47d4-427f-aa9e-16857b319069', 'test', 'e3661968-47d4-427f-aa9e-16857b319069');
INSERT INTO users (user_id, name, username) VALUES ('100f83b7-3f72-4012-a46d-591970e3bcd6', 'test', '100f83b7-3f72-4012-a46d-591970e3bcd6');
INSERT INTO sponsors (user_id) VALUES  ('e3661968-47d4-427f-aa9e-16857b319069');
INSERT INTO tributes (user_id) VALUES  ('100f83b7-3f72-4012-a46d-591970e3bcd6');
--no such tribute
INSERT INTO chats(chat_id, sponsor_id, tribute_id) VALUES ('5274cfca-be9b-4e73-b76f-c13ea70728a1','e3661968-47d4-427f-aa9e-16857b319069','5274cfca-be9b-4e73-b76f-c13ea70728a1');

--no such sponsor
INSERT INTO chats(chat_id, sponsor_id, tribute_id) VALUES ('5274cfca-be9b-4e73-b76f-c13ea70728a1','5274cfca-be9b-4e73-b76f-c13ea70728a1','100f83b7-3f72-4012-a46d-591970e3bcd6');

--valid insert
INSERT INTO chats(chat_id, sponsor_id, tribute_id) VALUES ('5274cfca-be9b-4e73-b76f-c13ea70728a1','e3661968-47d4-427f-aa9e-16857b319069','100f83b7-3f72-4012-a46d-591970e3bcd6');



-----table messages
INSERT INTO users (user_id, name, username) VALUES ('03a1cdad-73ba-4311-9d8f-d1274da10e4d', 'test', '03a1cdad-73ba-4311-9d8f-d1274da10e4d');
INSERT INTO users (user_id, name, username) VALUES ('280ed790-d095-4d29-8ed9-86850a59f56b', 'test', '280ed790-d095-4d29-8ed9-86850a59f56b');
INSERT INTO sponsors (user_id) VALUES  ('03a1cdad-73ba-4311-9d8f-d1274da10e4d');
INSERT INTO tributes (user_id) VALUES  ('280ed790-d095-4d29-8ed9-86850a59f56b');
INSERT INTO chats(chat_id, sponsor_id, tribute_id) VALUES ('0835d5ea-6031-4a32-ac96-1f535fc5c3f2','03a1cdad-73ba-4311-9d8f-d1274da10e4d','280ed790-d095-4d29-8ed9-86850a59f56b');
-- null date
INSERT INTO messages(message_id, message, chat_id, user_id) VALUES ('fd47a730-22d3-4413-bcf1-b7cb9f9aecfd','','0835d5ea-6031-4a32-ac96-1f535fc5c3f2','03a1cdad-73ba-4311-9d8f-d1274da10e4d');
-- null message
INSERT INTO messages(message_id, date_time, chat_id, user_id) VALUES ('fd47a730-22d3-4413-bcf1-b7cb9f9aecfd',CURRENT_TIMESTAMP,'0835d5ea-6031-4a32-ac96-1f535fc5c3f2','03a1cdad-73ba-4311-9d8f-d1274da10e4d');
-- no such user
INSERT INTO messages(message_id, date_time, message, chat_id, user_id) VALUES ('fd47a730-22d3-4413-bcf1-b7cb9f9aecfd',CURRENT_TIMESTAMP,'','0835d5ea-6031-4a32-ac96-1f535fc5c3f2','e252a1fc-750a-4488-886f-95a7f39dddb4');
-- no such chat
INSERT INTO messages(message_id, date_time, message, chat_id, user_id) VALUES ('fd47a730-22d3-4413-bcf1-b7cb9f9aecfd',CURRENT_TIMESTAMP,'','e252a1fc-750a-4488-886f-95a7f39dddb4','03a1cdad-73ba-4311-9d8f-d1274da10e4d');
--valid insert
INSERT INTO messages(message_id, date_time, message, chat_id, user_id) VALUES ('fd47a730-22d3-4413-bcf1-b7cb9f9aecfd',CURRENT_TIMESTAMP,'','0835d5ea-6031-4a32-ac96-1f535fc5c3f2','03a1cdad-73ba-4311-9d8f-d1274da10e4d');


-----table news
INSERT INTO users (user_id, name, username) VALUES ('6c2a89c5-7c96-4ea7-b358-74bbadf57167', 'test', '6c2a89c5-7c96-4ea7-b358-74bbadf57167');
INSERT INTO moderator (user_id) VALUES  ('6c2a89c5-7c96-4ea7-b358-74bbadf57167');
--no such moderator
insert into news(news_id, content, date_time, name, moderator_user_id) VALUES ('7398a867-ad84-42f7-ae48-f19098798915','',CURRENT_TIMESTAMP, '','7398a867-ad84-42f7-ae48-f19098798915');
--null/empty content
insert into news(news_id,  date_time, name, moderator_user_id) VALUES ('c46cd498-bde9-4fb5-a12f-5558f0c4423d',CURRENT_TIMESTAMP, '','6c2a89c5-7c96-4ea7-b358-74bbadf57167');
--valid insert
insert into news(news_id, content, date_time, name, moderator_user_id) VALUES ('7398a867-ad84-42f7-ae48-f19098798915','',CURRENT_TIMESTAMP, '','6c2a89c5-7c96-4ea7-b358-74bbadf57167');


----table news_subscription_orders
insert into orders(order_id, approved, paid, price) VALUES ('eeb94127-16a7-46b1-8c34-c2514b6d438e', null, true, 0);
INSERT INTO users (user_id, name, username) VALUES ('8b08603f-4008-4b1d-921f-c3059a56a6e8', 'test', '8b08603f-4008-4b1d-921f-c3059a56a6e8');
INSERT INTO sponsors (user_id) VALUES  ('8b08603f-4008-4b1d-921f-c3059a56a6e8');
--no such order
insert into news_subscription_orders(email, order_id, sponsor_user_id) VALUES ('example@mail.com', '1b51d46a-8a7b-43c7-a0eb-8421902c9193','8b08603f-4008-4b1d-921f-c3059a56a6e8');
--no such sponsor
insert into news_subscription_orders(email, order_id, sponsor_user_id) VALUES ('example@mail.com', 'eeb94127-16a7-46b1-8c34-c2514b6d438e','1b51d46a-8a7b-43c7-a0eb-8421902c9193');
--valid insert
insert into news_subscription_orders(email, order_id, sponsor_user_id) VALUES ('example@mail.com', 'eeb94127-16a7-46b1-8c34-c2514b6d438e','8b08603f-4008-4b1d-921f-c3059a56a6e8');





