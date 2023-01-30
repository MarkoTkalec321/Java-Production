DROP TABLE IF EXISTS STORE_ITEM;
DROP TABLE IF EXISTS STORE;
DROP TABLE IF EXISTS FACTORY_ITEM;
DROP TABLE IF EXISTS FACTORY;
DROP TABLE IF EXISTS ADDRESS;
DROP TABLE IF EXISTS ITEM;
DROP TABLE IF EXISTS CATEGORY;

CREATE TABLE CATEGORY(
ID LONG GENERATED ALWAYS AS IDENTITY,
NAME VARCHAR(25) NOT NULL,
DESCRIPTION VARCHAR(250) NOT NULL,
PRIMARY KEY(ID)
);

CREATE TABLE ITEM(
ID LONG GENERATED ALWAYS AS IDENTITY,
CATEGORY_ID LONG NOT NULL,
NAME VARCHAR(25) NOT NULL,
WIDTH DECIMAL(10,2) NOT NULL,
HEIGHT DECIMAL(10,2) NOT NULL,
LENGTH DECIMAL(10,2) NOT NULL,
PRODUCTION_COST DECIMAL(15,2) NOT NULL,
SELLING_PRICE DECIMAL(15,2) NOT NULL,
PRIMARY KEY (ID),
FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY(ID)
);

CREATE TABLE ADDRESS(
ID LONG GENERATED ALWAYS AS IDENTITY,
STREET VARCHAR(50) NOT NULL,
HOUSE_NUMBER VARCHAR(10) NOT NULL,
CITY VARCHAR(30) NOT NULL,
POSTAL_CODE INT NOT NULL,
PRIMARY KEY (ID)
);

CREATE TABLE FACTORY(
ID LONG GENERATED ALWAYS AS IDENTITY,
NAME VARCHAR(50) NOT NULL,
ADDRESS_ID LONG NOT NULL,
PRIMARY KEY (ID),
FOREIGN KEY (ADDRESS_ID) REFERENCES ADDRESS(ID)
);

CREATE TABLE FACTORY_ITEM(
FACTORY_ID LONG NOT NULL,
ITEM_ID LONG NOT NULL,
PRIMARY KEY (FACTORY_ID, ITEM_ID),
FOREIGN KEY (FACTORY_ID) REFERENCES FACTORY(ID),
FOREIGN KEY (ITEM_ID) REFERENCES ITEM(ID)
);

CREATE TABLE STORE(
ID LONG GENERATED ALWAYS AS IDENTITY,
NAME VARCHAR(50) NOT NULL,
WEB_ADDRESS VARCHAR(50) NOT NULL,
PRIMARY KEY (ID)
);

CREATE TABLE STORE_ITEM(
STORE_ID LONG NOT NULL,
ITEM_ID LONG NOT NULL,
PRIMARY KEY (STORE_ID, ITEM_ID),
FOREIGN KEY (STORE_ID) REFERENCES STORE(ID),
FOREIGN KEY (ITEM_ID) REFERENCES ITEM(ID)
);

INSERT INTO CATEGORY(NAME, DESCRIPTION) VALUES('Food', 'Category of edible items');
INSERT INTO CATEGORY(NAME, DESCRIPTION) VALUES('Technical Equipment', 'Technical equipment items');
INSERT INTO CATEGORY(NAME, DESCRIPTION) VALUES('Literature', 'Literature items');

INSERT INTO ITEM(CATEGORY_ID, NAME, WIDTH, HEIGHT, LENGTH, PRODUCTION_COST, SELLING_PRICE) VALUES(1, 'Sarma', 15.0, 5.0, 25.0, 10.0, 20.0);

INSERT INTO ITEM(CATEGORY_ID, NAME, WIDTH, HEIGHT, LENGTH, PRODUCTION_COST, SELLING_PRICE) VALUES(3, 'Dune', 6.1, 12.3, 15.2, 9.0, 21.3);
INSERT INTO ITEM(CATEGORY_ID, NAME, WIDTH, HEIGHT, LENGTH, PRODUCTION_COST, SELLING_PRICE) VALUES(1, 'Bolonjez', 44.2, 50.0, 7.7, 15.2, 11.2);
INSERT INTO ITEM(CATEGORY_ID, NAME, WIDTH, HEIGHT, LENGTH, PRODUCTION_COST, SELLING_PRICE) VALUES(3, 'Javantura', 51.3, 52.0, 22.0, 12.3, 23.1);
INSERT INTO ITEM(CATEGORY_ID, NAME, WIDTH, HEIGHT, LENGTH, PRODUCTION_COST, SELLING_PRICE) VALUES(2, 'Ventilator', 13.4, 33.0, 25.4, 11.0, 34.0);
INSERT INTO ITEM(CATEGORY_ID, NAME, WIDTH, HEIGHT, LENGTH, PRODUCTION_COST, SELLING_PRICE) VALUES(2, 'Olovka', 44.5, 23.0, 24.0, 56.5, 55.44);
INSERT INTO ITEM(CATEGORY_ID, NAME, WIDTH, HEIGHT, LENGTH, PRODUCTION_COST, SELLING_PRICE) VALUES(2, 'PS4 Controller', 23.6, 33.0, 22.0, 10.6, 66.3);
INSERT INTO ITEM(CATEGORY_ID, NAME, WIDTH, HEIGHT, LENGTH, PRODUCTION_COST, SELLING_PRICE) VALUES(2, 'Tepih', 45.7, 71.0, 21.0, 9.7, 8.1);
INSERT INTO ITEM(CATEGORY_ID, NAME, WIDTH, HEIGHT, LENGTH, PRODUCTION_COST, SELLING_PRICE) VALUES(2, 'Grah', 36.8, 9.0, 19.0, 3.8, 12.1);
INSERT INTO ITEM(CATEGORY_ID, NAME, WIDTH, HEIGHT, LENGTH, PRODUCTION_COST, SELLING_PRICE) VALUES(3, 'Biblija', 46.8, 91.0, 123.0, 5.8, 12.1);

INSERT INTO ADDRESS(STREET, HOUSE_NUMBER, CITY, POSTAL_CODE) VALUES('Ilica', '311', 'Đakovo', 31400);

INSERT INTO ADDRESS(STREET, HOUSE_NUMBER, CITY, POSTAL_CODE) VALUES('Mokrička', '11', 'Zagreb', 10000);
INSERT INTO ADDRESS(STREET, HOUSE_NUMBER, CITY, POSTAL_CODE) VALUES('Savska', '23', 'Vukovar', 32000);

INSERT INTO FACTORY(NAME, ADDRESS_ID) VALUES('Podravka', 1);
INSERT INTO FACTORY(NAME, ADDRESS_ID) VALUES('Superknjižara', 2);
INSERT INTO FACTORY(NAME, ADDRESS_ID) VALUES('NVIDIA', 3);

INSERT INTO FACTORY_ITEM(FACTORY_ID, ITEM_ID) VALUES(1, 1);

INSERT INTO FACTORY_ITEM(FACTORY_ID, ITEM_ID) VALUES(2, 2);
INSERT INTO FACTORY_ITEM(FACTORY_ID, ITEM_ID) VALUES(3, 3);
INSERT INTO FACTORY_ITEM(FACTORY_ID, ITEM_ID) VALUES(3, 4);
INSERT INTO FACTORY_ITEM(FACTORY_ID, ITEM_ID) VALUES(3, 5);

INSERT INTO STORE(NAME, WEB_ADDRESS) VALUES('Kupi sve', 'www.kupi-sve.hr');
INSERT INTO STORE(NAME, WEB_ADDRESS) VALUES('Prodaja Računala', 'www.prodajaracunala.hr');
INSERT INTO STORE(NAME, WEB_ADDRESS) VALUES('Links', 'www.links.hr');

INSERT INTO STORE_ITEM(STORE_ID, ITEM_ID) VALUES(1, 1);
INSERT INTO STORE_ITEM(STORE_ID, ITEM_ID) VALUES(2, 2);
INSERT INTO STORE_ITEM(STORE_ID, ITEM_ID) VALUES(3, 5);
INSERT INTO STORE_ITEM(STORE_ID, ITEM_ID) VALUES(3, 6);