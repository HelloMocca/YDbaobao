set SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
INSERT INTO CATEGORY VALUES (0, '미분류');
ALTER TABLE PRODUCTS ALTER COLUMN productPrice SET DEFAULT '0';
ALTER TABLE PRODUCTS ALTER COLUMN categoryId SET DEFAULT '0';
ALTER TABLE PRODUCTS MODIFY productDescription text;