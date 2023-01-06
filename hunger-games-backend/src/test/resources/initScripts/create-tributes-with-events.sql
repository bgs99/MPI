INSERT INTO users (user_id, name, username)
VALUES ('9667900f-24b2-4795-ad20-28b933d9ae32', 'tribute-test1', '9667900f-24b2-4795-ad20-28b933d9ae32');
INSERT INTO user_roles(user_id, role)
VALUES ('9667900f-24b2-4795-ad20-28b933d9ae32', 'TRIBUTE');
INSERT INTO tributes (user_id)
values ('9667900f-24b2-4795-ad20-28b933d9ae32');

INSERT INTO users (user_id, name, username)
VALUES ('c0b91cca-27ba-49d2-85e1-290cbd73d45e', 'tribute-test2', 'c0b91cca-27ba-49d2-85e1-290cbd73d45e');
INSERT INTO user_roles(user_id, role)
VALUES ('c0b91cca-27ba-49d2-85e1-290cbd73d45e', 'TRIBUTE');
INSERT INTO tributes (user_id)
values ('c0b91cca-27ba-49d2-85e1-290cbd73d45e');

INSERT INTO users (user_id, name, username)
VALUES ('3ee25464-bdec-4237-a335-94c1f376e8d6', 'tribute-test3', '3ee25464-bdec-4237-a335-94c1f376e8d6');
INSERT INTO user_roles(user_id, role)
VALUES ('3ee25464-bdec-4237-a335-94c1f376e8d6', 'TRIBUTE');
INSERT INTO tributes (user_id)
values ('3ee25464-bdec-4237-a335-94c1f376e8d6');

INSERT INTO events(event_id, date_time, event_place, event_type, tribute_id)
VALUES ('6a2a8ab8-4bd8-400f-bd7b-306ede0bb39a', CURRENT_DATE + INTERVAL '1' day, 'some place', 'MEETING',
        '9667900f-24b2-4795-ad20-28b933d9ae32');
INSERT INTO events(event_id, date_time, event_place, event_type, tribute_id)
VALUES ('78503b7f-8444-4f04-b927-79391199757e', CURRENT_DATE + INTERVAL '1' day, 'some place', 'INTERVIEW',
        '9667900f-24b2-4795-ad20-28b933d9ae32');
INSERT INTO events(event_id, date_time, event_place, event_type, tribute_id)
VALUES ('52a2febf-feb1-485c-8641-d7da61b767e9', CURRENT_DATE - INTERVAL '1' day, 'some place', 'INTERVIEW',
        '9667900f-24b2-4795-ad20-28b933d9ae32');

INSERT INTO events(event_id, date_time, event_place, event_type, tribute_id)
VALUES ('b97d1545-e96e-4e86-8711-5ae28bdba97a', CURRENT_DATE + INTERVAL '1' day, 'some place', 'MEETING',
        'c0b91cca-27ba-49d2-85e1-290cbd73d45e');
INSERT INTO events(event_id, date_time, event_place, event_type, tribute_id)
VALUES ('39e19181-e20e-4c6a-b6d4-10ada8891ecd', CURRENT_DATE + INTERVAL '1' day, 'some place', 'INTERVIEW',
        'c0b91cca-27ba-49d2-85e1-290cbd73d45e');
INSERT INTO events(event_id, date_time, event_place, event_type, tribute_id)
VALUES ('d5997e85-aaec-4af5-8c57-b0c4f4f81072', CURRENT_DATE - INTERVAL '1' day, 'some place', 'INTERVIEW',
        'c0b91cca-27ba-49d2-85e1-290cbd73d45e');

INSERT INTO events(event_id, date_time, event_place, event_type, tribute_id)
VALUES ('c4bab111-2c52-4ae3-9145-1fdb98400e2b', CURRENT_DATE + INTERVAL '1' day, 'some place', 'MEETING',
        '3ee25464-bdec-4237-a335-94c1f376e8d6');
INSERT INTO events(event_id, date_time, event_place, event_type, tribute_id)
VALUES ('fc2da925-046c-413a-a129-e72a9683c078', CURRENT_DATE + INTERVAL '1' day, 'some place', 'INTERVIEW',
        '3ee25464-bdec-4237-a335-94c1f376e8d6');
INSERT INTO events(event_id, date_time, event_place, event_type, tribute_id)
VALUES ('943d0959-1394-4f96-b7fe-5281ec093716', CURRENT_DATE - INTERVAL '1' day, 'some place', 'INTERVIEW',
        '3ee25464-bdec-4237-a335-94c1f376e8d6');

