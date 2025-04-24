INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
INSERT INTO roles(name) VALUES('ROLE_SUPERADMIN');

INSERT INTO clickmanager.address (id, city, country, postal_code, state, street)
VALUES (1, 'Fairfield', 'USA', '52557', 'IA', '1000 N 4th St');

INSERT INTO clickmanager.businesses (id, industry, name, address_id)
VALUES (1, 'The Owner', 'All Knowing.', 1);

INSERT INTO users (email, password, username, business_id)
VALUES ('superadmin@example.com', '$2a$10$z4u.bUn8.GivdL/I9.lLy.wP.tSd1Y0oqA8jRj700dlOXuV7yTF7e', 'rmss', 1);

INSERT INTO user_roles (user_id, role_id)
VALUES (LAST_INSERT_ID(), 4);
