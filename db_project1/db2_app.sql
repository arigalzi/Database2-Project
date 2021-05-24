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
`productID` INT NOT NULL,
`userID` INT NOT NULL ,
`answer` VARCHAR(50) NOT NULL,
PRIMARY KEY (`userID`, `questionID`,`productID`),
FOREIGN KEY (`questionID`) REFERENCES `Question` (`questionID`) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (`productID`) REFERENCES `Question` (`productID`) ON DELETE CASCADE ON UPDATE CASCADE,
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
`timestamp` TIMESTAMP ,
`userID` INT NOT NULL,
`formCancelled` BOOLEAN DEFAULT 0,
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


LOCK TABLES  `db2_app`.`User` WRITE;
/*!40000 ALTER TABLE `db2_app`.`User` DISABLE KEYS */;
INSERT INTO `db2_app`.`User` VALUES (1,'leog','leo','leonardo.giusti@mail.polimi.com',0,0), (2,'frag','fra','fra.gov@mail.polimi.com',0,1), (3, 'ari','arig','galzi.ari@mail.polimi.com',1,0), (4,'anon','none','anon.none@mail.polimi.com',1,1);
/*!40000 ALTER TABLE `db2_app`.`User` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES  `db2_app`.`Product` WRITE;
/*!40000 ALTER TABLE `db2_app`.`Product` DISABLE KEYS */;
INSERT INTO `db2_app`.`Product` VALUES (1,'2021/05/01','iphone','','Last phone of Apple'), (2,'2021/05/02','Samsung','','Last phone of Samsung'), (3,'2021/05/03','iphone','','Last phone of Motorola'), (4,'2021/05/13','LG','','Last phone of LG');
/*!40000 ALTER TABLE `db2_app`.`Product` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES  `db2_app`.`Question` WRITE;
/*!40000 ALTER TABLE `db2_app`.`Question` DISABLE KEYS */;
INSERT INTO `db2_app`.`Question` VALUES (1,0,'is it useful?',1, 2), (2,0,'how much?',2, 2), (3,0,'why?',3, 2), (4,0,'is it useful?',1, 3), (5,0,'how much?',2, 3), (6,0,'why?',3, 3), (7,0,'is it useful?',1, 1), (8,0,'how much?',2, 1), (9,0,'why?',3, 1);
/*!40000 ALTER TABLE `db2_app`.`Question` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES  `db2_app`.`Answer` WRITE;
/*!40000 ALTER TABLE `db2_app`.`Answer` DISABLE KEYS */;
INSERT INTO `db2_app`.`Answer` VALUES (1,2,1,'AAAA' ), (1,2,2,'BBB' ),(2,2,1,'ccc' ),(3,2,1,'ccc' ) ;
/*!40000 ALTER TABLE `db2_app`.`Answer` ENABLE KEYS */;
UNLOCK TABLES;


LOCK TABLES  `db2_app`.`Review` WRITE;
/*!40000 ALTER TABLE `db2_app`.`Review` DISABLE KEYS */;
INSERT INTO `db2_app`.`Review` VALUES (1,'Incredidible',1), (2,'WOW',1), (3,'NICE',1),(4,'Incredidible',2), (5,'WOW',2), (6,'NICEEEE',3);
/*!40000 ALTER TABLE `db2_app`.`Review` ENABLE KEYS */;
UNLOCK TABLES;


-- queries necessarie: tot punti di un utente per un questionario ; tot punti di utenti su un questionario ; prodotto del giorno;
-- verifica se in una answer c'e una offensive;

DROP TRIGGER IF EXISTS db2_app.UpdateEvaluation;

DELIMITER $$
CREATE TRIGGER UpdateEvaluation
    after INSERT on db2_app.Answer
    FOR EACH row
begin
    declare mandatory boolean;
    select q.isMandatory into mandatory
    from db2_app.answer a natural join db2_app.question q
    where a.userID =new.userID and a.productID = new.productID and q.questionID=new.questionID;

    if(not exists (select *
                   from db2_app.evaluation e
                   where e.userID =new.userID and e.productID = new.productID) ) then
        if (mandatory = true) then
            insert into db2_app.evaluation values(new.userID, new.productID, 1);
        else
            insert into db2_app.evaluation values(new.userID, new.productID, 2);
        end if;

    else
        if (mandatory = true) then
            update db2_app.evaluation set totalPoints = totalPoints+1 where userID =new.userID and productID = new.productID ;
        else
            update db2_app.evaluation set totalPoints = totalPoints+2 where userID =new.userID and productID = new.productID ;
        end if;
    end if;

end$$

DELIMITER ;
