DROP TABLE vet_specialties IF EXISTS;
DROP TABLE vets IF EXISTS;
DROP TABLE specialties IF EXISTS;
DROP TABLE visits IF EXISTS;
DROP TABLE pets IF EXISTS;
DROP TABLE types IF EXISTS;
DROP TABLE owners IF EXISTS;
DROP TABLE users IF EXISTS;
DROP TABLE authorities IF EXISTS;
DROP TABLE beautician IF EXISTS;
DROP TABLE beautician_specializations IF EXISTS;
DROP TABLE beauty_center IF EXISTS;
DROP TABLE pick_up_request IF EXISTS;
DROP TABLE product IF EXISTS;

CREATE TABLE vets (
  id         INTEGER IDENTITY PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR(30)
);
CREATE INDEX vets_last_name ON vets (last_name);

CREATE TABLE specialties (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX specialties_name ON specialties (name);

CREATE TABLE vet_specialties (
  vet_id       INTEGER NOT NULL,
  specialty_id INTEGER NOT NULL
);
ALTER TABLE vet_specialties ADD CONSTRAINT fk_vet_specialties_vets FOREIGN KEY (vet_id) REFERENCES vets (id);
ALTER TABLE vet_specialties ADD CONSTRAINT fk_vet_specialties_specialties FOREIGN KEY (specialty_id) REFERENCES specialties (id);

CREATE TABLE types (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX types_name ON types (name);

CREATE TABLE owners (
  id         INTEGER IDENTITY PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR_IGNORECASE(30),
  address    VARCHAR(255),
  city       VARCHAR(80),
  telephone  VARCHAR(20)
);
CREATE INDEX owners_last_name ON owners (last_name);

CREATE TABLE pets (
  id         INTEGER IDENTITY PRIMARY KEY,
  name       VARCHAR(30),
  birth_date DATE,
  type_id    INTEGER NOT NULL,
  owner_id   INTEGER NOT NULL
);
ALTER TABLE pets ADD CONSTRAINT fk_pets_owners FOREIGN KEY (owner_id) REFERENCES owners (id);
ALTER TABLE pets ADD CONSTRAINT fk_pets_types FOREIGN KEY (type_id) REFERENCES types (id);

CREATE TABLE visits (
  id          INTEGER IDENTITY PRIMARY KEY,
  pet_id      INTEGER NOT NULL,
  visit_date  DATE,
  description VARCHAR(255)
);
ALTER TABLE visits ADD CONSTRAINT fk_visits_pets FOREIGN KEY (pet_id) REFERENCES pets (id);
CREATE INDEX visits_pet_id ON visits (pet_id);

CREATE TABLE users(
	id INTEGER IDENTITY PRIMARY KEY,
	username varchar_ignorecase(255) NOT NULL,
	password varchar_ignorecase(255) NOT NULL,
	enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
	username varchar_ignorecase(50) NOT NULL,
	authority varchar_ignorecase(50) NOT NULL,	
);
ALTER TABLE authorities ADD CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users(username);

CREATE UNIQUE INDEX ix_auth_username ON authorities (username,authority);

CREATE TABLE beautician (
	  id         INTEGER IDENTITY PRIMARY KEY,
	  first_name VARCHAR(30),
	  last_name  VARCHAR(30),
	  user_id	INTEGER NOT NULL
);
ALTER TABLE beautician ADD CONSTRAINT fk_beautician_user_id FOREIGN KEY (user_id) REFERENCES users (id);
CREATE INDEX beautician_last_name ON beautician (last_name);

CREATE TABLE beautician_specializations (
  beautician_id       INTEGER NOT NULL,
  specializations_id INTEGER NOT NULL
);
ALTER TABLE beautician_specializations ADD CONSTRAINT fk_beautician_specializations FOREIGN KEY (beautician_id) REFERENCES beautician (id);
ALTER TABLE beautician_specializations ADD CONSTRAINT fk_beautician_specializations FOREIGN KEY (beautician_id) REFERENCES specialties (id);

CREATE TABLE beauty_center (
	  id         INTEGER IDENTITY PRIMARY KEY,
	  name       VARCHAR(30),
	  description VARCHAR(30),
	  pet_type_id    INTEGER NOT NULL,
	  beautician_id   INTEGER NOT NULL
);
ALTER TABLE beauty_center ADD CONSTRAINT fk_beauty_center_beautician FOREIGN KEY (beautician_id) REFERENCES beautician (id);
ALTER TABLE beauty_center ADD CONSTRAINT fk_beauty_center_pet_type_id FOREIGN KEY (pet_type_id) REFERENCES types (id);

CREATE TABLE pick_up_request (
	id	INTEGER IDENTITY PRIMARY KEY,
	description VARCHAR(30),
	pet_type_id INTEGER NOT NULL,
	physical_status VARCHAR(30),
	is_accepted BOOLEAN NOT NULL,
	address VARCHAR(30),
	is_closed BOOLEAN NOT NULL,
	contact VARCHAR(30),
	owner_id INTEGER NOT NULL
);
ALTER TABLE pick_up_request ADD CONSTRAINT fk_pick_up_request_owner FOREIGN KEY (owner_id) REFERENCES owner (id);
ALTER TABLE pick_up_request ADD CONSTRAINT fk_pick_up_request_pet_type_id FOREIGN KEY (pet_type_id) REFERENCES types (id);

CREATE TABLE products (
	id	INTEGER IDENTITY PRIMARY KEY,
	name VARCHAR(30),
	type_id INTEGER NOT NULL,
	description VARCHAR(30),
	avaliable BOOLEAN,
	beautician_id INTEGER NOT NULL
);
ALTER TABLE products ADD CONSTRAINT fk_products_type_id FOREIGN KEY (type_id) REFERENCES types (id);
ALTER TABLE products ADD CONSTRAINT fk_products_beautician FOREIGN KEY (beautician_id) REFERENCES beautician (id);