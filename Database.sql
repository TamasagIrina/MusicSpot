CREATE DATABASE  IF NOT EXISTS `music` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `music`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: music
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `friends`
--

DROP TABLE IF EXISTS `friends`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friends` (
  `friend_id` int NOT NULL AUTO_INCREMENT,
  `friend_name` varchar(225) DEFAULT NULL,
  `idUserAccounts` int unsigned DEFAULT NULL,
  PRIMARY KEY (`friend_id`),
  KEY `idUserAccounts` (`idUserAccounts`),
  CONSTRAINT `friends_ibfk_1` FOREIGN KEY (`idUserAccounts`) REFERENCES `useraccounts` (`idUserAccounts`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friends`
--

LOCK TABLES `friends` WRITE;
/*!40000 ALTER TABLE `friends` DISABLE KEYS */;
INSERT INTO `friends` VALUES (31,'irina',18),(32,'irinatam',1),(35,'irina',9),(36,'ana',1);
/*!40000 ALTER TABLE `friends` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friendsrequest`
--

DROP TABLE IF EXISTS `friendsrequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friendsrequest` (
  `friend_id` int NOT NULL AUTO_INCREMENT,
  `friend_name` varchar(225) DEFAULT NULL,
  `idUserAccounts` int unsigned DEFAULT NULL,
  PRIMARY KEY (`friend_id`),
  KEY `idUserAccounts` (`idUserAccounts`),
  CONSTRAINT `friendsrequest_ibfk_1` FOREIGN KEY (`idUserAccounts`) REFERENCES `useraccounts` (`idUserAccounts`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friendsrequest`
--

LOCK TABLES `friendsrequest` WRITE;
/*!40000 ALTER TABLE `friendsrequest` DISABLE KEYS */;
/*!40000 ALTER TABLE `friendsrequest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `musiclist`
--

DROP TABLE IF EXISTS `musiclist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `musiclist` (
  `music_id` int NOT NULL AUTO_INCREMENT,
  `music_name` varchar(255) DEFAULT NULL,
  `music_artist` varchar(255) DEFAULT NULL,
  `music_img` varchar(255) DEFAULT NULL,
  `music_file` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`music_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `musiclist`
--

LOCK TABLES `musiclist` WRITE;
/*!40000 ALTER TABLE `musiclist` DISABLE KEYS */;
INSERT INTO `musiclist` VALUES (1,'Let\'s Travel','by Declan DP','C:\\Users\\irina\\IdeaProjects\\MusicSpot\\src\\img1.png','LetsTravel.mp3'),(2,'Want You','by Vendredi & Sterkol','C:\\Users\\irina\\IdeaProjects\\MusicSpot\\src\\img.png','WantYou.mp3'),(3,'Soul mates','by DayFox & BraveLion','C:\\Users\\irina\\IdeaProjects\\MusicSpot\\src\\img3.png','Soulmates.mp3'),(4,'Bang a Pop','by Jessie K','C:\\Users\\irina\\IdeaProjects\\MusicSpot\\src\\pBangPop.png','bangaPop.mp3'),(5,'My Music','by Rome & Igual','C:\\Users\\irina\\IdeaProjects\\MusicSpot\\src\\pozaMyMusic.jpg','myMusic.mp3');
/*!40000 ALTER TABLE `musiclist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `playlist`
--

DROP TABLE IF EXISTS `playlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `playlist` (
  `playlist_id` int NOT NULL AUTO_INCREMENT,
  `music_name` varchar(255) DEFAULT NULL,
  `idUserAccounts` int unsigned DEFAULT NULL,
  PRIMARY KEY (`playlist_id`),
  KEY `idUserAccounts` (`idUserAccounts`),
  CONSTRAINT `playlist_ibfk_1` FOREIGN KEY (`idUserAccounts`) REFERENCES `useraccounts` (`idUserAccounts`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `playlist`
--

LOCK TABLES `playlist` WRITE;
/*!40000 ALTER TABLE `playlist` DISABLE KEYS */;
INSERT INTO `playlist` VALUES (1,'bangaPop.mp3',1),(2,'LetsTravel.mp3',1),(3,'bangaPop.mp3',4),(4,'LetsTravel.mp3',4),(5,'myMusic.mp3',4),(6,'myMusic.mp3',1),(7,'bangaPop.mp3',3),(8,'bangaPop.mp3',9),(9,'Soulmates.mp3',9),(10,'WantYou.mp3',1);
/*!40000 ALTER TABLE `playlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `useraccounts`
--

DROP TABLE IF EXISTS `useraccounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `useraccounts` (
  `idUserAccounts` int unsigned NOT NULL AUTO_INCREMENT,
  `FirstName` varchar(45) NOT NULL,
  `LastName` varchar(45) NOT NULL,
  `UserName` varchar(45) NOT NULL,
  `Password` varchar(45) NOT NULL,
  PRIMARY KEY (`idUserAccounts`),
  UNIQUE KEY `idUserAccounts_UNIQUE` (`idUserAccounts`),
  UNIQUE KEY `UserName_UNIQUE` (`UserName`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useraccounts`
--

LOCK TABLES `useraccounts` WRITE;
/*!40000 ALTER TABLE `useraccounts` DISABLE KEYS */;
INSERT INTO `useraccounts` VALUES (1,'Irina','Tamasag','irina','irinat'),(3,'Teo','Binisor','teo','bines'),(4,'irina','binisor','irinab','binisor'),(5,'Irinat','Tamasagg','irinat','irinat'),(6,'teo','Binisor','Teodor','bines'),(7,'na','bebe','nana','be'),(8,'irina','Tamasag','Irina123','irinat'),(9,'ana','banana','ana','anab'),(10,'ana','banana2','ana2','anab'),(11,'Z','z','zx','z'),(12,'ana21','banana1','ana22','anab1'),(14,'nana','ene','nana2','ene2'),(15,'ana','mere','anamere','ana1'),(16,'sa','as','asd','asd'),(17,'as','ss','sas','ss'),(18,'Irina','tamasag','irinatam','irinat');
/*!40000 ALTER TABLE `useraccounts` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-25 14:27:43
