-- On H2 by Default, data.sql script will get executed before Hibernate is initialized
-- or setting spring property to spring.sql.init.mode=always

insert into users(id, username, password) values
(1, 'root', '{bcrypt}$2a$10$wGZ.YRgPc1qcpTDjPcgDb.aI6wvtCPec9rUnDvO7XyHfk6Rux.l8e'),
(2, 'user', '{bcrypt}$2a$10$u4QezkXfy0iLUd95ZhYzQel/pVoOwM1u2Ox35njjeI6sjRRGLeK2S'),
(3, 'scott', '{bcrypt}$2a$10$wMM/q6qnAeHEL7hb.Ivfsu9soKfqQxKGRYSUstgYAxOEae7MLeKNa');

insert into authorities(id, role) values
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER'),
(3, 'ROLE_CUSTOMER');

insert into user_authorities(user_id, role_id) values
(1, 1),
(1, 2),
(1, 3),
(2, 2),
(3, 3);