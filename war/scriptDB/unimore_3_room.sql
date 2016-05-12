-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: unimore_3
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
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room` (
  `room_id` int(11) NOT NULL AUTO_INCREMENT,
  `room_code` varchar(20) NOT NULL,
  `room_number` int(11) NOT NULL,
  `room_floor` int(11) NOT NULL,
  `building_number` int(11) NOT NULL,
  `room_maxPeople` int(11) NOT NULL DEFAULT '1',
  `room_dimension` int(11) NOT NULL DEFAULT '0',
  `socket_network` int(11) NOT NULL DEFAULT '0',
  `maintenance` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`room_id`),
  UNIQUE KEY `room_code` (`room_code`),
  UNIQUE KEY `room_number` (`room_number`,`room_floor`,`building_number`),
  KEY `ibfk_3` (`building_number`),
  CONSTRAINT `ibfk_3` FOREIGN KEY (`building_number`) REFERENCES `building` (`building_number`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,'10',1,1,1,2,20,4,'nulla'),(2,'11',2,1,1,3,15,2,'da riverniciare'),(3,'15',2,0,1,5,10,0,NULL),(4,'fac-0-1-10',10,1,1,0,0,0,'nessun particolare'),(5,'fac-0-1-23',23,1,1,0,0,0,'nessun particolare'),(6,'fac-0-1-3',3,1,1,0,0,0,'nessun particolare'),(7,'fac-0-1-4',4,1,1,0,0,0,'nessun particolare'),(8,'fac-0-1-5',5,1,1,0,0,0,'nessun particolare'),(9,'fac-0-1-6',6,1,1,0,0,0,'nessun particolare'),(10,'fac-0-1-7',7,1,1,0,0,0,'nessun particolare'),(11,'fac-0-1-8',8,1,1,0,0,0,'nessun particolare'),(12,'fac-0-1-9',9,1,1,0,0,0,'nessun particolare'),(13,'fac-0-1-11',11,1,1,0,0,0,'nessun particolare'),(14,'fac-0-1-12',12,1,1,0,0,0,'nessun particolare'),(15,'fac-0-1-14',14,1,1,0,0,0,'nessun particolare'),(16,'fac-0-1-13',13,1,1,0,0,0,'nessun particolare'),(17,'fac-0-1-15',15,1,1,0,0,0,'nessun particolare'),(18,'fac-0-1-16',16,1,1,0,0,0,'nessun particolare'),(19,'fac-0-1-17',17,1,1,0,0,0,'nessun particolare'),(20,'fac-0-1-18',18,1,1,0,0,0,'nessun particolare'),(21,'fac-0-1-19',19,1,1,0,0,0,'nessun particolare'),(22,'fac-0-1-20',20,1,1,0,0,0,'nessun particolare'),(23,'fac-0-1-21',21,1,1,0,0,0,'nessun particolare'),(24,'fac-0-1-22',22,1,1,0,0,0,'nessun particolare'),(25,'fac-0-1-24',24,1,1,0,0,0,'nessun particolare'),(26,'fac-0-1-25',25,1,1,0,0,0,'nessun particolare'),(27,'fac-0-1-32',32,1,1,0,0,0,'nessun particolare'),(28,'fac-0-1-33',33,1,1,0,0,0,'nessun particolare'),(29,'fac-0-1-31',31,1,1,0,0,0,'nessun particolare'),(30,'fac-0-1-30',30,1,1,0,0,0,'nessun particolare'),(31,'fac-0-1-28',28,1,1,0,0,0,'nessun particolare'),(32,'fac-0-1-29',29,1,1,0,0,0,'nessun particolare'),(33,'fac-0-1-27',27,1,1,0,0,0,'nessun particolare'),(34,'fac-0-1-34',34,1,1,0,0,0,'nessun particolare'),(35,'fac-0-1-35',35,1,1,0,0,0,'nessun particolare'),(36,'fac-0-1-36',36,1,1,0,0,0,'nessun particolare'),(37,'fac-0-1-39',39,1,1,0,0,0,'nessun particolare'),(38,'fac-0-1-38',38,1,1,0,0,0,'nessun particolare');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-05-12 21:48:38
