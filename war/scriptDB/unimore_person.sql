-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: unimore
-- ------------------------------------------------------
-- Server version	5.7.11-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `person_id` int(11) NOT NULL AUTO_INCREMENT,
  `person_name` char(30) NOT NULL,
  `person_surname` char(30) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`person_id`),
  UNIQUE KEY `person_name` (`person_name`,`person_surname`),
  KEY `ibfk_4` (`role_id`),
  CONSTRAINT `ibfk_4` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (1,'luca','ferrari',1),(2,'mario','cipollini',5),(3,'luigi','ferraris',1),(4,'saverio','massimini',3),(5,'gigi','verdi',2),(6,'luca','corda',1),(7,'giulio','cesare',1),(8,'mario','rossini',3),(9,'gino','facchinetti',1),(10,'paolo','zucchi',5),(11,'paolo','ruini',1),(12,'maria','casolari',4),(13,'mario','venturelli',2),(14,'lugi','rossi',1),(15,'mario','amadori',2),(16,'leonardo','quasimodo',1),(17,'giorgio','conrad',5),(18,'isac','asimov',2),(19,'giuseppe','lorenzi',2),(20,'marco','pascoli',3),(21,'saverio','rossi',5),(22,'luigi','neri',1),(23,'riccardo','vitali',2),(24,'loris','tondi',4),(25,'federico','lucarelli',5),(26,'fabio','ricci',1),(27,'roberto','guidi',2),(28,'nadia','vandelli',4),(29,'sabina','ruggeri',5),(30,'eleonora','zaccaria',1),(31,'paola','minelli',3),(32,'claudia','baselli',4),(33,'erica','ferrari',2);
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-04-21 22:43:26
