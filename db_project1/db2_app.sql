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

-- Table `Question`
DROP TABLE IF EXISTS `db2_app.Question` ;
CREATE TABLE IF NOT EXISTS `db2_app`.`Question`(
	  `questionID` INT NOT NULL AUTO_INCREMENT,
      `date` DATE,
	  `isMandatory` BOOLEAN DEFAULT 0, -- 0 for mandatory for the marketing 1 for stats fixed
	  `text` VARCHAR(50 )NOT NULL,
	  `questionNumber` INT NOT NULL,
      `productID` int not null,
	  FOREIGN KEY (`productID`) REFERENCES `Product` (`productID`),
	  PRIMARY KEY ( `questionID` , `date`)
);

-- Table `Questionnaire`
DROP TABLE IF EXISTS `db2_app.Questionnaire` ;
CREATE TABLE IF NOT EXISTS `db2_app`.`Questionnaire`( -- N:N REL between question and user
	  `questionID` INT NOT NULL,
	  `userID`  INT NOT NULL,
      `date` DATE,
      `productID` int not null,
	  FOREIGN KEY (`date`) REFERENCES `Question` (`date`) ON DELETE CASCADE ON UPDATE CASCADE,
	  FOREIGN KEY (`questionID`) REFERENCES `Question` (`questionID`) ON DELETE CASCADE ON UPDATE CASCADE,
	  FOREIGN KEY (`userID`) REFERENCES `User` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE,
	  PRIMARY KEY (`productID`, `userID`, `date`)
);

 -- Table `Product`
DROP TABLE IF EXISTS `db2_app.Product` ;
CREATE TABLE IF NOT EXISTS `db2_app`.`Product`(
	  `productID` INT NOT NULL AUTO_INCREMENT,
	  `name` VARCHAR(50) NOT NULL,
	  `image` longblob,
	  `description` VARCHAR(256),
	  PRIMARY KEY (`productID`)
);

DROP TABLE IF EXISTS `db2_app.Answer` ;
CREATE TABLE IF NOT EXISTS `db2_app`.`Answer`(
	  `questionID` INT NOT NULL,
	  `userID` INT NOT NULL ,
	  `answer` VARCHAR(50) NOT NULL,
	  `point` INT DEFAULT 0,
      `isSubmitted` BOOLEAN DEFAULT 0,
	  FOREIGN KEY (`questionID`) REFERENCES `Question` (`questionID`) ON DELETE CASCADE ON UPDATE CASCADE,
	  FOREIGN KEY (`userID`) REFERENCES `User` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE,
	  PRIMARY KEY (`userID`, `questionID`)
);

-- Table `DirtyWord`
DROP TABLE IF EXISTS `db2_app.DirtyWord` ;
CREATE TABLE IF NOT EXISTS `db2_app`.`DirtyWord`(
	  `dirtyID` INT NOT NULL AUTO_INCREMENT,
	  `text` VARCHAR(50) NOT NULL,
	  PRIMARY KEY (`dirtyID`)
);

-- Table `Review`
-- Weak Entity
DROP TABLE IF EXISTS `db2_app.Review` ;
CREATE TABLE IF NOT EXISTS `db2_app`.`Review`(
	  `reviewID` INT NOT NULL AUTO_INCREMENT,
	  `text` VARCHAR(50) NOT NULL,
	  `productID` INT NOT NULL,
	  FOREIGN KEY (`productID`) REFERENCES `Product` (`productID`) ON DELETE CASCADE ON UPDATE CASCADE,
	  PRIMARY KEY (`productID`, `reviewID`)
);

-- Table `Log`
DROP TABLE IF EXISTS `db2_app.Log` ;
CREATE TABLE IF NOT EXISTS `db2_app`.`Log`(
	  `logID` INT NOT NULL AUTO_INCREMENT,
	  `timestamp` TIME NOT NULL,
	  `userID` INT NOT NULL,
	   FOREIGN KEY (`userID`) REFERENCES `User` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE,
	   PRIMARY KEY (`logID`)
);


-- queries necessarie: tot punti di un utente per un questionario ; tot punti di utenti su un questionario ; prodotto del giorno;
-- verifica se in una answer c'e una offensive;

