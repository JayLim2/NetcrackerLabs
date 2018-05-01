SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = ON;
SET search_path = public, pg_catalog;
SET check_function_bodies = FALSE;
SET client_min_messages = WARNING;
SET row_security = OFF;

--DROP TABLE userList;

CREATE SEQUENCE IF NOT EXISTS auto_increment_publisher
  START WITH 0
  INCREMENT BY 1
  MINVALUE 0
  MAXVALUE 9223372036854775801
  CACHE 1;

CREATE SEQUENCE IF NOT EXISTS auto_increment_book
  START WITH 0
  INCREMENT BY 1
  MINVALUE 0
  MAXVALUE 9223372036854775801
  CACHE 1;

CREATE SEQUENCE IF NOT EXISTS auto_increment_author
  START WITH 0
  INCREMENT BY 1
  MINVALUE 0
  MAXVALUE 9223372036854775801
  CACHE 1;

CREATE SEQUENCE IF NOT EXISTS auto_increment_user
  START WITH 0
  INCREMENT BY 1
  MINVALUE 0
  MAXVALUE 9223372036854775801
  CACHE 1;


ALTER TABLE auto_increment_publisher
  OWNER TO postgres;

ALTER TABLE auto_increment_book
  OWNER TO postgres;

ALTER TABLE auto_increment_author
  OWNER TO postgres;

ALTER TABLE auto_increment_user
  OWNER TO postgres;


SET default_tablespace = '';

SET default_with_oids = FALSE;


CREATE TABLE IF NOT EXISTS publisher (
  "publisherID"   BIGINT DEFAULT nextval('auto_increment_publisher' :: REGCLASS) NOT NULL,
  "publisherName" CHARACTER VARYING(30)                                          NOT NULL UNIQUE,
  PRIMARY KEY ("publisherID")
);

CREATE TABLE IF NOT EXISTS userList (
  "userID"      INTEGER DEFAULT nextval('auto_increment_user' :: REGCLASS)        NOT NULL,
  "userLogin"   CHARACTER VARYING(30)                                             NOT NULL UNIQUE,
  "userPass"    CHARACTER VARYING(230)                                            NOT NULL,
  "cart"        INTEGER                                                              ,
  "role"        CHARACTER VARYING(30)                                                 ,
  PRIMARY KEY ("userID")
);

CREATE TABLE IF NOT EXISTS book (
  "bookID"      BIGINT DEFAULT nextval('auto_increment_book' :: REGCLASS) NOT NULL,
  "bookName"    CHARACTER VARYING(50)                                     NOT NULL UNIQUE,
  "publishYear" INTEGER                                                   NOT NULL,
  "brief"       CHARACTER VARYING(2500)                                   NOT NULL,
  "publisherID" BIGINT                                                    NOT NULL,
  PRIMARY KEY ("bookID"),
  FOREIGN KEY ("publisherID") REFERENCES "publisher" ("publisherID")
);


CREATE TABLE IF NOT EXISTS author (
  "authorID"   BIGINT DEFAULT nextval('auto_increment_author' :: REGCLASS) NOT NULL,
  "authorName" CHARACTER VARYING(60)                                       NOT NULL UNIQUE,
  PRIMARY KEY ("authorID")
);

CREATE TABLE IF NOT EXISTS "authorBookConnector" (
  "authorID" BIGINT NOT NULL,
  "bookID"   BIGINT NOT NULL,


  FOREIGN KEY ("authorID") REFERENCES author ("authorID"),
  FOREIGN KEY ("bookID") REFERENCES book ("bookID"),
  PRIMARY KEY ("authorID", "bookID")
);

ALTER TABLE author
  OWNER TO postgres;


ALTER TABLE "authorBookConnector"
  OWNER TO postgres;


ALTER TABLE book
  OWNER TO postgres;


ALTER TABLE publisher
  OWNER TO postgres;



CREATE INDEX IF NOT EXISTS "fki_authorID"
  ON "authorBookConnector" USING BTREE ("authorID");


CREATE INDEX IF NOT EXISTS "fki_bookID"
  ON "authorBookConnector" USING BTREE ("bookID");


CREATE INDEX IF NOT EXISTS "fki_publisherID"
  ON book USING BTREE ("publisherID");





-- USERS

CREATE SEQUENCE IF NOT EXISTS auto_increment_user
  START WITH 0
  INCREMENT BY 1
  MINVALUE 0
  MAXVALUE 9223372036854775801
  CACHE 1;

CREATE SEQUENCE IF NOT EXISTS auto_increment_role
  START WITH 0
  INCREMENT BY 1
  MINVALUE 0
  MAXVALUE 9223372036854775801
  CACHE 1;

ALTER TABLE auto_increment_user
  OWNER TO postgres;
ALTER TABLE auto_increment_role
  OWNER TO postgres;



CREATE TABLE IF NOT EXISTS "role" (
  "role_id"    INTEGER DEFAULT nextval('auto_increment_user' :: REGCLASS)  NOT NULL,
  "role" CHARACTER VARYING(255)                                         ,
  PRIMARY KEY ("role_id")
);

CREATE TABLE IF NOT EXISTS "users" (
  "user_id"    INTEGER DEFAULT nextval('auto_increment_user' :: REGCLASS)  NOT NULL,
  "active" INTEGER                                         ,
  "email"  CHARACTER VARYING(255) NOT NULL,
  "last_name" CHARACTER VARYING(255) NOT NULL,
  "name"      CHARACTER VARYING(255) NOT NULL,
  "password"    CHARACTER VARYING(255) NOT NULL,
  PRIMARY KEY ("user_id")
);

INSERT INTO "role" VALUES (1,'ADMIN');
INSERT INTO "role" VALUES (2,'ROLE_ADMIN');

INSERT INTO "users" (active, email, last_name, name, password) VALUES ('1', 'admin@admin.ru',
                                                                       'admin', 'admin',
                                                                       '$2a$10$9H1hL/JIUm.Jj4GNGKUTXehPSdFZD9vIu1axShVE8zJM6gOVRTVRO');

CREATE TABLE IF NOT EXISTS "user_role" (
  "user_id"    INTEGER   NOT NULL,
  "role_id" INTEGER       NOT NULL,
  PRIMARY KEY ("user_id","role_id"),
  --
  CONSTRAINT "FK859n2jvi8ivhui0rl0esws6o"
  FOREIGN KEY ("user_id") REFERENCES "users" ("user_id"),
  CONSTRAINT "FKa68196081fvovjhkek5m97n3y"
  FOREIGN KEY ("role_id") REFERENCES "role" ("role_id")
);

INSERT INTO "user_role" VALUES (0,2);







