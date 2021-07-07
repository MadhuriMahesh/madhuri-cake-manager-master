CREATE SCHEMA IF NOT EXISTS main;



DROP TABLE IF EXISTS main.CAKE;

CREATE TABLE main.CAKE (
  `ID` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `CAKE_NAME` varchar(50) NOT NULL,
  `DESC` varchar(256) DEFAULT NULL,
  `IMG`  varchar(4096) DEFAULT NULL,
  `USER_ID` varchar(50) DEFAULT NULL,
  PRIMARY KEY (ID)
);

DROP TABLE IF EXISTS main.USER;

CREATE TABLE main.USER (
                           `ID` varchar(50) DEFAULT NULL ,
                           `NAME` varchar(50) DEFAULT NULL,
                           `EMAIL` varchar(256) DEFAULT NULL,
                           PRIMARY KEY (ID)
);


ALTER TABLE main.CAKE ADD CONSTRAINT FK_CAKE_USER FOREIGN KEY (USER_ID) REFERENCES main.USER(ID) ON DELETE CASCADE;

