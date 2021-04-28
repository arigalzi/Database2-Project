DROP SCHEMA IF EXISTS `db2_app`;
CREATE SCHEMA IF NOT EXISTS `db2_app` DEFAULT CHARACTER SET utf8 ;
USE `db2_app` ;

-- Table `User`
DROP TABLE IF EXISTS `db2_app.User` ;
CREATE TABLE IF NOT EXISTS `db2_app`.`User` (
`userID` INT NOT NULL AUTO_INCREMENT,
`username` VARCHAR(50) NOT NULL,
`password` VARCHAR(50)NOT NULL,
`email` VARCHAR(50) NOT NULL,
`isBanned` BOOLEAN DEFAULT 0,
`isAdmin` BOOLEAN DEFAULT 0,
PRIMARY KEY (`userID`)
);

-- Table `Product`
DROP TABLE IF EXISTS `db2_app.Product` ;
CREATE TABLE IF NOT EXISTS `db2_app`.`Product`(
`productID` INT NOT NULL AUTO_INCREMENT,
`date` DATE,
`name` VARCHAR(50) NOT NULL,
`image` longblob,
`description` VARCHAR(256),
PRIMARY KEY (`productID`)
);

-- Table `Question`
DROP TABLE IF EXISTS `db2_app.Question` ;
CREATE TABLE IF NOT EXISTS `db2_app`.`Question`(
`questionID` INT NOT NULL AUTO_INCREMENT,
`isMandatory` BOOLEAN DEFAULT 0, -- 0 for mandatory for the marketing 1 for stats fixed
`text` VARCHAR(50 )NOT NULL,
`questionNumber` INT NOT NULL,
`productID` int not null,
PRIMARY KEY ( `questionID` ,`productID`),
FOREIGN KEY (`productID`) REFERENCES `Product` (`productID`)


);


DROP TABLE IF EXISTS `db2_app.Answer` ;
CREATE TABLE IF NOT EXISTS `db2_app`.`Answer`(
`questionID` INT NOT NULL,
`userID` INT NOT NULL ,
`answer` VARCHAR(50) NOT NULL,
`isSubmitted` BOOLEAN DEFAULT 0,
PRIMARY KEY (`userID`, `questionID`),
FOREIGN KEY (`questionID`) REFERENCES `Question` (`questionID`) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (`userID`) REFERENCES `User` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Table `DirtyWord`
DROP TABLE IF EXISTS `db2_app.DirtyWord` ;
CREATE TABLE IF NOT EXISTS `db2_app`.`DirtyWord`(
`dirtyID` INT NOT NULL AUTO_INCREMENT,
`text` VARCHAR(50) NOT NULL,
PRIMARY KEY (`dirtyID`)
);

-- Table `Log`
DROP TABLE IF EXISTS `db2_app.Log` ;
CREATE TABLE IF NOT EXISTS `db2_app`.`Log`(
`logID` INT NOT NULL AUTO_INCREMENT,
`timestamp` TIME ,
`userID` INT NOT NULL,
PRIMARY KEY (`logID`),
FOREIGN KEY (`userID`) REFERENCES `User` (`userID`)  ON DELETE CASCADE ON UPDATE CASCADE
);


-- Table `Review`
-- Weak Entity
DROP TABLE IF EXISTS `db2_app.Review` ;
CREATE TABLE IF NOT EXISTS `db2_app`.`Review`(
`reviewID` INT NOT NULL AUTO_INCREMENT,
`text` VARCHAR(50) NOT NULL,
`productID` INT NOT NULL,
PRIMARY KEY (`reviewID`, `productID`),
FOREIGN KEY (`productID`) REFERENCES `Product` (`productID`) ON DELETE CASCADE ON UPDATE CASCADE
);


DROP TABLE IF EXISTS `db2_app.Evaluation` ;
CREATE TABLE IF NOT EXISTS `db2_app`.`Evaluation`(
`userID` INT NOT NULL,
`productID` INT NOT NULL ,
`totalPoints` INT NOT NULL,
PRIMARY KEY (`userID`, `productID`),
FOREIGN KEY (`userID`) REFERENCES `User` (`userID`)  ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (`productID`) REFERENCES `Product` (`productID`) ON DELETE CASCADE ON UPDATE CASCADE
);



-- queries necessarie: tot punti di un utente per un questionario ; tot punti di utenti su un questionario ; prodotto del giorno;
-- verifica se in una answer c'e una offensive;