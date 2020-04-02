CREATE DATABASE IF NOT EXISTS customers;
USE customers;

CREATE TABLE t_customer(
	cid		CHAR(32) PRIMARY KEY,
	cname		VARCHAR(40) NOT NULL,
	gender		VARCHAR(6) NOT NULL,
	birthday	CHAR(10),
	cellphone	VARCHAR(15) NOT NULL,
	email		VARCHAR(40),		
	description	VARCHAR(501)
);

SELECT * FROM t_customer;