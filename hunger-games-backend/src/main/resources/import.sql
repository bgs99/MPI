--Settings:
INSERT INTO settings (setting_id, email) VALUES ('cfa34cc0-b9a5-4d89-ad84-b51ad6a433fb', 'mentor-name-1@capitol.com');
INSERT INTO settings (setting_id, email) VALUES ('de014ce3-0b1c-46bc-ad11-7d8af196aaf5', 'mentor-name-2@capitol.com');
INSERT INTO settings (setting_id, email) VALUES ('130604ab-bbd4-418f-89a9-27d6ceb78567', 'tribute-name-1@capitol.com');
INSERT INTO settings (setting_id, email) VALUES ('5540f25b-fd17-4be6-8b64-2dfdfae73eb3', 'tribute-name-2@capitol.com');
INSERT INTO settings (setting_id, email) VALUES ('6a403638-1d11-4328-9746-c97633d02dd4', 'moderator-name-1@capitol.com');

--Mentors:
INSERT INTO users (user_id, name, username, avatar_uri, setting_id) VALUES ('d7f8e213-8d00-417c-89b3-94eea56b152b', 'mentor-name-1', 'mentor-1', 'https://www.wallpaperflare.com/static/642/294/549/cat-grass-leaves-autumn-wallpaper.jpg', 'cfa34cc0-b9a5-4d89-ad84-b51ad6a433fb');
INSERT INTO users (user_id, name, username, avatar_uri, setting_id) VALUES ('2a20a33a-bca2-4a18-92a4-625784637a85', 'mentor-name-2', 'mentor-2', 'https://www.wallpaperflare.com/static/642/294/549/cat-grass-leaves-autumn-wallpaper.jpg', 'de014ce3-0b1c-46bc-ad11-7d8af196aaf5');
INSERT INTO user_roles (user_id, role) VALUES ('d7f8e213-8d00-417c-89b3-94eea56b152b', 'MENTOR');
INSERT INTO user_roles (user_id, role) VALUES ('2a20a33a-bca2-4a18-92a4-625784637a85', 'MENTOR');
INSERT INTO mentors (user_id, district) values  ('d7f8e213-8d00-417c-89b3-94eea56b152b','1');
INSERT INTO mentors (user_id, district) values  ('2a20a33a-bca2-4a18-92a4-625784637a85','2');
--Tributes:
INSERT INTO users (user_id, name, username, avatar_uri, setting_id) VALUES ('effa6ed2-1457-4bdd-a53d-c7b73137f917', 'tribute-name-1', 'tribute-1', 'https://www.wallpaperflare.com/static/642/294/549/cat-grass-leaves-autumn-wallpaper.jpg', '130604ab-bbd4-418f-89a9-27d6ceb78567');
INSERT INTO users (user_id, name, username, avatar_uri, setting_id) VALUES ('9539c9f8-2771-45ed-a8e2-b7a1c353f5bd', 'tribute-name-2', 'tribute-2', 'https://www.wallpaperflare.com/static/642/294/549/cat-grass-leaves-autumn-wallpaper.jpg', '5540f25b-fd17-4be6-8b64-2dfdfae73eb3');
INSERT INTO user_roles(user_id, role) VALUES ('effa6ed2-1457-4bdd-a53d-c7b73137f917', 'TRIBUTE');
INSERT INTO user_roles(user_id, role) VALUES ('9539c9f8-2771-45ed-a8e2-b7a1c353f5bd', 'TRIBUTE');
INSERT INTO tributes (user_id,mentor_user_id) values  ('effa6ed2-1457-4bdd-a53d-c7b73137f917','d7f8e213-8d00-417c-89b3-94eea56b152b');
INSERT INTO tributes (user_id,mentor_user_id) values  ('9539c9f8-2771-45ed-a8e2-b7a1c353f5bd','2a20a33a-bca2-4a18-92a4-625784637a85');
--Moderators:
INSERT INTO users (user_id, name, username, avatar_uri, setting_id) VALUES ('ce178138-a54d-4e52-bca5-88bd911f46ff', 'moderator-name-1', 'moderator-1', 'https://www.wallpaperflare.com/static/642/294/549/cat-grass-leaves-autumn-wallpaper.jpg', '6a403638-1d11-4328-9746-c97633d02dd4');
INSERT INTO user_roles(user_id, role) VALUES ('ce178138-a54d-4e52-bca5-88bd911f46ff', 'MODERATOR');
INSERT INTO moderator (user_id) values  ('ce178138-a54d-4e52-bca5-88bd911f46ff');

--Resources:
INSERT INTO resources (resource_id, name, price) VALUES ('a7ea0f48-eadb-46e7-9642-13998c6613cf', 'Resource-1', 50);
INSERT INTO resources (resource_id, name, price) VALUES ('dc43d8f5-7b08-48d5-a082-2286ef5e345d', 'Resource-2', 100);
INSERT INTO resources (resource_id, name, price) VALUES ('19a3ab8f-ef5f-4f8a-bb8e-eb28d45ce9ac', 'Resource-3', 150);
INSERT INTO resources (resource_id, name, price) VALUES ('ce371850-aa6d-4e42-9097-0cec8cc6769e', 'Resource-4', 200);
INSERT INTO resources (resource_id, name, price) VALUES ('90734358-23e8-4d0c-be7b-3918ef6157b5', 'Resource-5', 300);