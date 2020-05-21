INSERT IGNORE INTO users(id, username,password,enabled) VALUES (3, 'vet1','v3t',TRUE);
INSERT IGNORE INTO authorities VALUES ('vet1','vet');

INSERT IGNORE INTO vets (id,
  first_name,
  last_name, user_id) VALUES (1, 'James', 'Carter',3);
INSERT IGNORE INTO vets (id,
  first_name,
  last_name, user_id) VALUES (2, 'Helen', 'Leary',3);
INSERT IGNORE INTO vets (id,
  first_name,
  last_name, user_id) VALUES (3, 'Linda', 'Douglas',3);
INSERT IGNORE INTO vets (id,
  first_name,
  last_name, user_id) VALUES (4, 'Rafael', 'Ortega',3);
INSERT IGNORE INTO vets (id,
  first_name,
  last_name, user_id) VALUES (5, 'Henry', 'Stevens',3);
INSERT IGNORE INTO vets (id,
  first_name,
  last_name, user_id) VALUES (6, 'Sharon', 'Jenkins',3);

INSERT IGNORE INTO specialties (
  id,
  name
) VALUES (1, 'radiology');
INSERT IGNORE INTO specialties (
  id,
  name
) VALUES (2, 'surgery');
INSERT IGNORE INTO specialties (
  id,
  name
) VALUES (3, 'dentistry');

INSERT IGNORE INTO vet_specialties (
  vet_id,
  specialty_id
) VALUES (2, 1);
INSERT IGNORE INTO vet_specialties (
  vet_id,
  specialty_id
)  VALUES (3, 2);
INSERT IGNORE INTO vet_specialties (
  vet_id,
  specialty_id
) VALUES (3, 3);
INSERT IGNORE INTO vet_specialties (
  vet_id,
  specialty_id
)  VALUES (4, 2);
INSERT IGNORE INTO vet_specialties (
  vet_id,
  specialty_id
)  VALUES (5, 1);

INSERT IGNORE INTO types (id,name) VALUES (1, 'cat');
INSERT IGNORE INTO types (id,name) VALUES (2, 'dog');
INSERT IGNORE INTO types (id,name) VALUES (3, 'lizard');
INSERT IGNORE INTO types (id,name) VALUES (4, 'snake');
INSERT IGNORE INTO types (id,name) VALUES (5, 'bird');
INSERT IGNORE INTO types (id,name) VALUES (6, 'hamster');



INSERT IGNORE INTO users(id, username,password,enabled) VALUES (1, 'admin1','4dm1n',TRUE);
INSERT IGNORE INTO authorities VALUES ( 'admin1','admin');

INSERT IGNORE INTO users(id, username,password,enabled) VALUES (2, 'owner1','0wn3r',TRUE);
INSERT IGNORE INTO authorities VALUES ('owner1','owner');

INSERT IGNORE INTO users(id, username,password,enabled) VALUES (9, 'owner2','0wn3r',TRUE);
INSERT IGNORE INTO authorities VALUES ('owner2','owner');

INSERT IGNORE INTO users(id, username,password,enabled) VALUES (10, 'owner3','0wn3r',TRUE);
INSERT IGNORE INTO authorities VALUES ('owner3','owner');

INSERT IGNORE INTO users(id, username,password,enabled) VALUES (11, 'owner4','0wn3r',TRUE);
INSERT IGNORE INTO authorities VALUES ('owner4','owner');

INSERT IGNORE INTO users(id, username,password,enabled) VALUES (12, 'owner5','0wn3r',TRUE);
INSERT IGNORE INTO authorities VALUES ('owner5','owner');

INSERT IGNORE INTO users(id, username,password,enabled) VALUES (13, 'owner6','0wn3r',TRUE);
INSERT IGNORE INTO authorities VALUES ('owner6','owner');

INSERT IGNORE INTO users(id, username,password,enabled) VALUES (14, 'owner7','0wn3r',TRUE);
INSERT IGNORE INTO authorities VALUES ('owner7','owner');

INSERT IGNORE INTO users(id, username,password,enabled) VALUES (15, 'owner8','0wn3r',TRUE);
INSERT IGNORE INTO authorities VALUES ('owner8','owner');

INSERT IGNORE INTO users(id, username,password,enabled) VALUES (16, 'owner9','0wn3r',TRUE);
INSERT IGNORE INTO authorities VALUES ('owner9','owner');

INSERT IGNORE INTO users(id, username,password,enabled) VALUES (17, 'owner10','0wn3r',TRUE);
INSERT IGNORE INTO authorities VALUES ('owner10','owner');

INSERT IGNORE INTO owners (
  id,
  first_name,
  last_name,
  address,
  city,
  telephone, user_id) VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 2);
INSERT IGNORE INTO owners (
  id,
  first_name,
  last_name,
  address,
  city,
  telephone, user_id) VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 9);
INSERT IGNORE INTO owners (
  id,
  first_name,
  last_name,
  address,
  city,
  telephone, user_id) VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 10);
INSERT IGNORE INTO owners (
  id,
  first_name,
  last_name,
  address,
  city,
  telephone, user_id) VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 11);
INSERT IGNORE INTO owners (
  id,
  first_name,
  last_name,
  address,
  city,
  telephone, user_id) VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 12);
INSERT IGNORE INTO owners (
  id,
  first_name,
  last_name,
  address,
  city,
  telephone, user_id) VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 13);
INSERT IGNORE INTO owners (
  id,
  first_name,
  last_name,
  address,
  city,
  telephone, user_id) VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 14);
INSERT IGNORE INTO owners (
  id,
  first_name,
  last_name,
  address,
  city,
  telephone, user_id) VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 15);
INSERT IGNORE INTO owners (
  id,
  first_name,
  last_name,
  address,
  city,
  telephone, user_id) VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 16);
INSERT IGNORE INTO owners (
  id,
  first_name,
  last_name,
  address,
  city,
  telephone, user_id) VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 17);

INSERT IGNORE INTO pets (
  id,
  name,
  birth_date,
  type_id,
  owner_id
) VALUES (1, 'Leo', '2000-09-07', 1, 1);
INSERT IGNORE INTO pets (
  id,
  name,
  birth_date,
  type_id,
  owner_id
) VALUES (2, 'Basil', '2002-08-06', 6, 2);
INSERT IGNORE INTO pets (
  id,
  name,
  birth_date,
  type_id,
  owner_id
) VALUES (3, 'Rosy', '2001-04-17', 2, 3);
INSERT IGNORE INTO pets (
  id,
  name,
  birth_date,
  type_id,
  owner_id
) VALUES (4, 'Jewel', '2000-03-07', 2, 3);
INSERT IGNORE INTO pets (
  id,
  name,
  birth_date,
  type_id,
  owner_id
) VALUES (5, 'Iggy', '2000-11-30', 3, 4);
INSERT IGNORE INTO pets (
  id,
  name,
  birth_date,
  type_id,
  owner_id
) VALUES (6, 'George', '2000-01-20', 4, 5);
INSERT IGNORE INTO pets (
  id,
  name,
  birth_date,
  type_id,
  owner_id
) VALUES (7, 'Samantha', '1995-09-04', 1, 6);
INSERT IGNORE INTO pets (
  id,
  name,
  birth_date,
  type_id,
  owner_id
) VALUES (8, 'Max', '1995-09-04', 1, 6);
INSERT IGNORE INTO pets (
  id,
  name,
  birth_date,
  type_id,
  owner_id
) VALUES (9, 'Lucky', '1999-08-06', 5, 7);
INSERT IGNORE INTO pets (
  id,
  name,
  birth_date,
  type_id,
  owner_id
) VALUES (10, 'Mulligan', '1997-02-24', 2, 8);
INSERT IGNORE INTO pets (
  id,
  name,
  birth_date,
  type_id,
  owner_id
) VALUES (11, 'Freddy', '2000-03-09', 5, 9);
INSERT IGNORE INTO pets (
  id,
  name,
  birth_date,
  type_id,
  owner_id
) VALUES (12, 'Lucky', '2000-06-24', 2, 10);
INSERT IGNORE INTO pets (
  id,
  name,
  birth_date,
  type_id,
  owner_id
) VALUES (13, 'Sly', '2002-06-08', 1, 10);

INSERT IGNORE INTO visits (
  id,
  pet_id,
  visit_date,
  description) VALUES (1, 7, '2010-03-04', 'rabies shot');
INSERT IGNORE INTO visits (
  id,
  pet_id,
  visit_date,
  description) VALUES (2, 8, '2011-03-04', 'rabies shot');
INSERT IGNORE INTO visits (
  id,
  pet_id,
  visit_date,
  description) VALUES (3, 8, '2009-06-04', 'neutered');
INSERT IGNORE INTO visits (
  id,
  pet_id,
  visit_date,
  description) VALUES (4, 7, '2008-09-04', 'spayed');

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

INSERT IGNORE INTO beauty_date(id,description,start_date,pet_id,beauty_center_id) VALUES (1,'prueba','2020-04-01 16:00',1,1);

INSERT IGNORE INTO pick_up_request(id,description,pet_type_id,physical_status,is_accepted,address,is_closed,contact,owner_id) VALUES (1, 'prueba1', 1, 'prueba2', TRUE, 'prueba3', FALSE, 'prueba4', 1);
INSERT IGNORE INTO pick_up_request(id,description,pet_type_id,physical_status,is_accepted,address,is_closed,contact,owner_id) VALUES (2, 'prueba5', 2, 'prueba6', FALSE, 'prueba7', TRUE, 'prueba8', 2);
INSERT IGNORE INTO pick_up_request(id,description,pet_type_id,physical_status,is_accepted,address,is_closed,contact,owner_id) VALUES (3, 'prueba1', 1, 'prueba2', FALSE, 'prueba3', FALSE, 'prueba4', 1);
INSERT IGNORE INTO pick_up_request(id,description,pet_type_id,physical_status,is_accepted,address,is_closed,contact,owner_id) VALUES (4, 'prueba1', 1, 'prueba2', FALSE, 'prueba3', FALSE, 'prueba4', 1);

INSERT IGNORE INTO products(id,name,type_id,description,avaliable,beautician_id) VALUES (1,'prueba1',1,'prueba2',TRUE,1);
INSERT IGNORE INTO products(id,name,type_id,description,avaliable,beautician_id) VALUES (2,'prueba3',2,'prueba4',TRUE,2);