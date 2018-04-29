SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = ON;
SET search_path = public, pg_catalog;
SET check_function_bodies = FALSE;
SET client_min_messages = WARNING;
SET row_security = OFF;

!--DROP TABLE userList;

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
  "role"        INTEGER                                                              ,
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

ALTER TABLE userlist
  OWNER TO postgres;


CREATE INDEX IF NOT EXISTS "fki_authorID"
  ON "authorBookConnector" USING BTREE ("authorID");


CREATE INDEX IF NOT EXISTS "fki_bookID"
  ON "authorBookConnector" USING BTREE ("bookID");


CREATE INDEX IF NOT EXISTS "fki_publisherID"
  ON book USING BTREE ("publisherID");