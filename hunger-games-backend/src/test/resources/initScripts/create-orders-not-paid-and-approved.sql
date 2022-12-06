INSERT INTO resources (resource_id, name, price) VALUES ('33ff5ee9-c0d7-4955-b2cd-a0aa3d484b98', 'Resource-test-1', 50);
INSERT INTO resources (resource_id, name, price) VALUES ('47f75e81-4f14-4af5-bce2-b6d5af372d94', 'Resource-test-2', 100);

INSERT INTO users (user_id, name, username) VALUES ('4a9f1d37-c6fd-4391-8082-655bb98fb460', 'sponsor-test', 'sponsor-name');
INSERT INTO user_roles(user_id, role) VALUES ('4a9f1d37-c6fd-4391-8082-655bb98fb460', 'SPONSOR');
INSERT INTO sponsors (user_id) values  ('4a9f1d37-c6fd-4391-8082-655bb98fb460');

INSERT INTO users (user_id, name, username) VALUES ('1d3ad419-e98f-43f1-9ac6-08776036cded', 'mentor-test', 'mentor-name');
INSERT INTO user_roles(user_id, role) VALUES ('1d3ad419-e98f-43f1-9ac6-08776036cded', 'MENTOR');
INSERT INTO mentors (user_id, district) values  ('1d3ad419-e98f-43f1-9ac6-08776036cded', '1');

INSERT INTO users (user_id, name, username) VALUES ('9667900f-24b2-4795-ad20-28b933d9ae32', 'tribute-test', 'tribute-name');
INSERT INTO user_roles(user_id, role) VALUES ('9667900f-24b2-4795-ad20-28b933d9ae32', 'TRIBUTE');
INSERT INTO tributes (user_id, mentor_user_id) values  ('9667900f-24b2-4795-ad20-28b933d9ae32', '1d3ad419-e98f-43f1-9ac6-08776036cded');

INSERT INTO orders(order_id, approved, paid, price, tribute_user_id) VALUES ('3d6e3de8-3311-4d89-9c92-4b5bf13f55c7', true, false, 250, '9667900f-24b2-4795-ad20-28b933d9ae32');
INSERT INTO resource_orders(order_id) VALUES('3d6e3de8-3311-4d89-9c92-4b5bf13f55c7');

INSERT INTO orders(order_id, approved, paid, price, tribute_user_id) VALUES ('61091dd1-84db-44ae-a7fe-4d98316a63cc', true, false, 200, '9667900f-24b2-4795-ad20-28b933d9ae32');
INSERT INTO resource_orders(order_id) VALUES('61091dd1-84db-44ae-a7fe-4d98316a63cc');

INSERT INTO order_details(order_detail_id, size, resource_resource_id, order_id) VALUES ('a11bddd0-5b7c-4231-897b-a7e484467948', 1, '33ff5ee9-c0d7-4955-b2cd-a0aa3d484b98', '61091dd1-84db-44ae-a7fe-4d98316a63cc');
INSERT INTO order_details(order_detail_id, size, resource_resource_id, order_id) VALUES ('61653708-23f6-404b-bbe4-4a5b954522b5', 2, '47f75e81-4f14-4af5-bce2-b6d5af372d94', '61091dd1-84db-44ae-a7fe-4d98316a63cc');
INSERT INTO order_details(order_detail_id, size, resource_resource_id, order_id) VALUES ('8c5ee8dd-f2bc-4cba-9db0-4426eb446782', 1,'47f75e81-4f14-4af5-bce2-b6d5af372d94', '3d6e3de8-3311-4d89-9c92-4b5bf13f55c7');
INSERT INTO order_details(order_detail_id, size, resource_resource_id, order_id) VALUES ('0147528f-8e41-4307-bccd-9f77e2470e71', 2,'33ff5ee9-c0d7-4955-b2cd-a0aa3d484b98', '3d6e3de8-3311-4d89-9c92-4b5bf13f55c7');
