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
-- Table structure for table `occupyroom`
--

DROP TABLE IF EXISTS `occupyroom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `occupyroom` (
  `occupyroom_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `person_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  PRIMARY KEY (`occupyroom_id`),
  KEY `ibfk_6` (`person_id`),
  KEY `ibfk_7` (`room_id`),
  CONSTRAINT `ibfk_6` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`),
  CONSTRAINT `ibfk_7` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `occupyroom`
--

LOCK TABLES `occupyroom` WRITE;
/*!40000 ALTER TABLE `occupyroom` DISABLE KEYS */;
INSERT INTO `occupyroom` VALUES (1,2,4),(2,3,4),(3,4,2),(4,5,8),(5,6,2),(6,7,4),(7,1,11),(8,1,10),(9,2,11),(10,3,11),(11,4,11),(12,5,11),(13,4,10),(14,6,11),(15,7,12),(16,8,16),(17,9,18),(18,10,18),(19,11,18),(20,12,14),(21,13,25),(22,14,26),(23,15,26),(24,16,27),(25,17,28),(26,18,28),(27,19,29),(28,20,30),(29,21,31),(30,22,32),(31,23,33),(32,24,33),(33,25,33),(34,26,33),(35,28,17),(36,25,17),(37,29,17),(38,31,17),(39,21,17);
/*!40000 ALTER TABLE `occupyroom` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-04-21 22:43:28
