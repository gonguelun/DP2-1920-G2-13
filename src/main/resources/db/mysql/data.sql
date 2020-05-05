INSERT IGNORE INTO vets VALUES (1, 'James', 'Carter');
INSERT IGNORE INTO vets VALUES (2, 'Helen', 'Leary');
INSERT IGNORE INTO vets VALUES (3, 'Linda', 'Douglas');
INSERT IGNORE INTO vets VALUES (4, 'Rafael', 'Ortega');
INSERT IGNORE INTO vets VALUES (5, 'Henry', 'Stevens');
INSERT IGNORE INTO vets VALUES (6, 'Sharon', 'Jenkins');

INSERT IGNORE INTO specialties VALUES (1, 'radiology');
INSERT IGNORE INTO specialties VALUES (2, 'surgery');
INSERT IGNORE INTO specialties VALUES (3, 'dentistry');

INSERT IGNORE INTO vet_specialties VALUES (2, 1);
INSERT IGNORE INTO vet_specialties VALUES (3, 2);
INSERT IGNORE INTO vet_specialties VALUES (3, 3);
INSERT IGNORE INTO vet_specialties VALUES (4, 2);
INSERT IGNORE INTO vet_specialties VALUES (5, 1);

INSERT IGNORE INTO types VALUES (1, 'cat');
INSERT IGNORE INTO types VALUES (2, 'dog');
INSERT IGNORE INTO types VALUES (3, 'lizard');
INSERT IGNORE INTO types VALUES (4, 'snake');
INSERT IGNORE INTO types VALUES (5, 'bird');
INSERT IGNORE INTO types VALUES (6, 'hamster');

INSERT IGNORE INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023');
INSERT IGNORE INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749');
INSERT IGNORE INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763');
INSERT IGNORE INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198');
INSERT IGNORE INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765');
INSERT IGNORE INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654');
INSERT IGNORE INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387');
INSERT IGNORE INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683');
INSERT IGNORE INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435');
INSERT IGNORE INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487');

INSERT IGNORE INTO pets VALUES (1, 'Leo', '2000-09-07', 1, 1);
INSERT IGNORE INTO pets VALUES (2, 'Basil', '2002-08-06', 6, 2);
INSERT IGNORE INTO pets VALUES (3, 'Rosy', '2001-04-17', 2, 3);
INSERT IGNORE INTO pets VALUES (4, 'Jewel', '2000-03-07', 2, 3);
INSERT IGNORE INTO pets VALUES (5, 'Iggy', '2000-11-30', 3, 4);
INSERT IGNORE INTO pets VALUES (6, 'George', '2000-01-20', 4, 5);
INSERT IGNORE INTO pets VALUES (7, 'Samantha', '1995-09-04', 1, 6);
INSERT IGNORE INTO pets VALUES (8, 'Max', '1995-09-04', 1, 6);
INSERT IGNORE INTO pets VALUES (9, 'Lucky', '1999-08-06', 5, 7);
INSERT IGNORE INTO pets VALUES (10, 'Mulligan', '1997-02-24', 2, 8);
INSERT IGNORE INTO pets VALUES (11, 'Freddy', '2000-03-09', 5, 9);
INSERT IGNORE INTO pets VALUES (12, 'Lucky', '2000-06-24', 2, 10);
INSERT IGNORE INTO pets VALUES (13, 'Sly', '2002-06-08', 1, 10);

INSERT IGNORE INTO visits VALUES (1, 7, '2010-03-04', 'rabies shot');
INSERT IGNORE INTO visits VALUES (2, 8, '2011-03-04', 'rabies shot');
INSERT IGNORE INTO visits VALUES (3, 8, '2009-06-04', 'neutered');
INSERT IGNORE INTO visits VALUES (4, 7, '2008-09-04', 'spayed');

INSERT IGNORE INTO users(id, username,password,enabled) VALUES (4, 'f','f',TRUE);
INSERT IGNORE INTO authorities VALUES ('f','beautician');
INSERT IGNORE INTO users(id, username,password,enabled) VALUES (5, 'g','g',TRUE);
INSERT IGNORE INTO authorities VALUES ('g','beautician');
INSERT IGNORE INTO users(id, username,password,enabled) VALUES (6, 'a','a',TRUE);
INSERT IGNORE INTO authorities VALUES ('a','beautician');
INSERT IGNORE INTO users(id, username,password,enabled) VALUES (7, 'c','c',TRUE);
INSERT IGNORE INTO authorities VALUES ('c','beautician');
INSERT IGNORE INTO users(id, username,password,enabled) VALUES (8, 's','s',TRUE);
INSERT IGNORE INTO authorities VALUES ('s','beautician');

INSERT IGNORE INTO beautician(id,first_name,last_name,user_id) VALUES (1, 'f', 'f', 4);
INSERT IGNORE INTO beautician(id,first_name,last_name,user_id) VALUES (2, 'g', 'g', 5);
INSERT IGNORE INTO beautician(id,first_name,last_name,user_id) VALUES (3, 'a', 'a', 6);
INSERT IGNORE INTO beautician(id,first_name,last_name,user_id) VALUES (4, 'c', 'c', 7);
INSERT IGNORE INTO beautician(id,first_name,last_name,user_id) VALUES (5, 's', 's', 8);

INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (1, 1);
INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (1, 3);
INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (1, 4);
INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (1, 5);
INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (1, 6);

INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (2, 1);
INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (2, 3);
INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (2, 4);
INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (2, 5);
INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (2, 6);

INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (3, 1);
INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (3, 3);
INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (3, 4);
INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (3, 5);
INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (3, 6);

INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (4, 1);
INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (4, 3);
INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (4, 4);
INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (4, 5);
INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (4, 6);

INSERT IGNORE INTO beautician_specializations(beautician_id,specializations_id) VALUES (5, 2);

INSERT IGNORE INTO beauty_center(id,name,description,pet_type_id,beautician_id) VALUES (1, 'name1', 'hey', 1, 1);
INSERT IGNORE INTO beauty_center(id,name,description,pet_type_id,beautician_id) VALUES (2, 'name1', 'hey', 3, 2);
INSERT IGNORE INTO beauty_center(id,name,description,pet_type_id,beautician_id) VALUES (3, 'name1', 'hey', 4, 3);
INSERT IGNORE INTO beauty_center(id,name,description,pet_type_id,beautician_id) VALUES (4, 'name1', 'hey', 5, 4);
INSERT IGNORE INTO beauty_center(id,name,description,pet_type_id,beautician_id) VALUES (5, 'name1', 'hey', 2, 5);

INSERT IGNORE INTO pick_up_request(id,description,pet_type_id,physical_status,is_accepted,address,is_closed,contact,owner_id) VALUES (1, 'prueba1', 1, 'prueba2', TRUE, 'prueba3', FALSE, 'prueba4', 1);
INSERT IGNORE INTO pick_up_request(id,description,pet_type_id,physical_status,is_accepted,address,is_closed,contact,owner_id) VALUES (2, 'prueba5', 2, 'prueba6', FALSE, 'prueba7', TRUE, 'prueba8', 2);
