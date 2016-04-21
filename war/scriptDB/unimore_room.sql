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
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room` (
  `room_id` int(11) NOT NULL AUTO_INCREMENT,
  `room_number` int(11) NOT NULL,
  `room_floor` int(11) NOT NULL,
  `building_number` int(11) NOT NULL,
  `room_maxPeople` int(11) NOT NULL,
  `room_dimension` int(11) NOT NULL,
  PRIMARY KEY (`room_id`),
  UNIQUE KEY `room_number` (`room_number`,`room_floor`,`building_number`),
  KEY `ibfk_3` (`building_number`),
  CONSTRAINT `ibfk_3` FOREIGN KEY (`building_number`) REFERENCES `building` (`building_number`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,1,0,1,2,20),(2,2,0,1,2,20),(3,3,0,1,3,20),(4,1,1,1,2,20),(5,1,4,1,2,25),(6,1,2,3,3,30),(7,2,1,1,2,20),(8,3,1,1,2,25),(9,4,1,1,2,20),(10,5,1,1,2,15),(11,6,1,1,2,20),(12,7,1,1,2,20),(14,32,1,1,3,35),(15,31,1,1,3,25),(16,9,1,1,3,30),(17,10,1,1,4,50),(18,11,1,1,2,15),(19,12,1,1,1,14),(20,13,1,1,1,15),(21,14,1,1,3,20),(22,20,1,1,3,20),(23,19,1,1,4,30),(24,21,1,1,3,20),(25,22,1,1,5,50),(26,23,1,1,3,30),(27,18,1,1,3,30),(28,17,1,1,3,20),(29,16,1,1,2,20),(30,15,1,1,3,20),(31,25,1,1,2,20),(32,24,1,1,2,20),(33,33,1,1,1,15),(34,30,1,1,1,15),(35,29,1,1,1,20),(36,28,1,1,3,30),(37,27,1,1,3,30),(38,38,1,1,2,20),(39,39,1,1,2,20),(40,34,1,1,3,30),(41,35,1,1,4,30),(42,36,1,1,4,30),(43,37,1,1,3,30),(44,8,1,1,0,0);
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

-- Dump completed on 2016-04-21 22:43:32
