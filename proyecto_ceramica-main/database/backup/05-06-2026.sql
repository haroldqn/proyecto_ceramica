-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: ceramic_store_db
-- ------------------------------------------------------
-- Server version	8.0.45

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
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id_category` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `event_status` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id_category`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Figuras de cerámica en forma de animales',_binary '','Animales','/categorias/animales.png','Animales'),(2,'Piezas de cerámica con formas de frutas',_binary '','Frutas','/categorias/frutas.png','Frutas'),(3,'Piezas de cerámica con diseños temáticos variados',_binary '','Piezas tematicas','/categorias/piezas_tematicas.png','Piezas tematicas');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id_order_item` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int NOT NULL,
  `unit_price` double NOT NULL,
  `id_order` int DEFAULT NULL,
  `id_product` bigint DEFAULT NULL,
  PRIMARY KEY (`id_order_item`),
  KEY `FKj18ef1agdhkb3f8rmgrrgdbvu` (`id_order`),
  KEY `FK2eoo5mp3khsag7l93o41u1w7g` (`id_product`),
  CONSTRAINT `FK2eoo5mp3khsag7l93o41u1w7g` FOREIGN KEY (`id_product`) REFERENCES `products` (`id_product`),
  CONSTRAINT `FKj18ef1agdhkb3f8rmgrrgdbvu` FOREIGN KEY (`id_order`) REFERENCES `orders` (`id_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id_order` int NOT NULL AUTO_INCREMENT,
  `register_date` datetime(6) NOT NULL,
  `status` varchar(255) NOT NULL,
  `total` double NOT NULL,
  `id_client` bigint DEFAULT NULL,
  PRIMARY KEY (`id_order`),
  KEY `FKij55jgmxvqv003tvf8yyj8k00` (`id_client`),
  CONSTRAINT `FKij55jgmxvqv003tvf8yyj8k00` FOREIGN KEY (`id_client`) REFERENCES `personas` (`id_persona`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id_payment` bigint NOT NULL AUTO_INCREMENT,
  `method` varchar(255) NOT NULL,
  `payment_date` datetime(6) NOT NULL,
  `id_order` int DEFAULT NULL,
  PRIMARY KEY (`id_payment`),
  KEY `FKalvupkbf37ax1kxiayasm8k1x` (`id_order`),
  CONSTRAINT `FKalvupkbf37ax1kxiayasm8k1x` FOREIGN KEY (`id_order`) REFERENCES `orders` (`id_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personas`
--

DROP TABLE IF EXISTS `personas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personas` (
  `id_persona` bigint NOT NULL AUTO_INCREMENT,
  `birth_date` varchar(255) DEFAULT NULL,
  `dni` varchar(8) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `mother_last_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id_persona`),
  UNIQUE KEY `UKnnr7w7h44crpy3wdqmk1724hp` (`dni`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personas`
--

LOCK TABLES `personas` WRITE;
/*!40000 ALTER TABLE `personas` DISABLE KEYS */;
INSERT INTO `personas` VALUES (1,'25/11/2003','73332055','JOSE ANTHONY','ADANAQUE','ZUÑIGA','JOSE ANTHONY ADANAQUE ZUÑIGA'),(2,NULL,NULL,'Angelo','V','','Angelo V');
/*!40000 ALTER TABLE `personas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_sizes`
--

DROP TABLE IF EXISTS `product_sizes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_sizes` (
  `id_product` bigint NOT NULL,
  `id_size` bigint NOT NULL,
  KEY `FKmy9ovi72qttbx2a18j8aix75j` (`id_size`),
  KEY `FKlkkrna8rmondt3jxb7u6csxfx` (`id_product`),
  CONSTRAINT `FKlkkrna8rmondt3jxb7u6csxfx` FOREIGN KEY (`id_product`) REFERENCES `products` (`id_product`),
  CONSTRAINT `FKmy9ovi72qttbx2a18j8aix75j` FOREIGN KEY (`id_size`) REFERENCES `size` (`id_size`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_sizes`
--

LOCK TABLES `product_sizes` WRITE;
/*!40000 ALTER TABLE `product_sizes` DISABLE KEYS */;
INSERT INTO `product_sizes` VALUES (1,3),(1,4),(2,1),(2,2),(3,3),(3,4),(4,3),(4,4),(5,2),(5,3),(6,1),(6,2),(7,3),(8,1),(9,2),(10,2),(11,3),(11,4),(11,5),(12,2),(12,3),(13,2),(13,3),(14,3),(15,2),(15,3),(16,1),(16,2),(17,4),(18,4),(18,5),(19,1),(19,2),(20,2),(20,3),(21,4),(21,5),(22,3),(22,4),(22,5),(23,4),(23,5),(24,3),(24,4),(25,4),(25,5),(26,3),(27,3),(28,2),(28,3),(29,2),(29,3),(30,2),(30,3);
/*!40000 ALTER TABLE `product_sizes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id_product` bigint NOT NULL AUTO_INCREMENT,
  `image_url` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `status` bit(1) NOT NULL,
  `stock` int NOT NULL,
  `id_category` bigint DEFAULT NULL,
  PRIMARY KEY (`id_product`),
  KEY `FKip7b0y8ja7fsm5wl7mhmseh5n` (`id_category`),
  CONSTRAINT `FKip7b0y8ja7fsm5wl7mhmseh5n` FOREIGN KEY (`id_category`) REFERENCES `categories` (`id_category`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'/categorias/loro.png','Loro azul',85.50,_binary '',12,1),(2,'/categorias/delfin.png','Delfin rosadita',15.90,_binary '',12,1),(3,'/categorias/perro.png','Perro fiel',45.00,_binary '',8,1),(4,'/categorias/buho.png','Buho sabio',55.00,_binary '',10,1),(5,'/categorias/dije_animal.png','Dije de Animal',25.00,_binary '',15,1),(6,'/categorias/pulga_animal.png','Pulga Animal',18.00,_binary '',20,1),(7,'/categorias/small_animal.png','Small Animal',30.00,_binary '',12,1),(8,'/categorias/super_small_animal.png','Super Small Animal',12.00,_binary '',25,1),(9,'/categorias/pulga_animal2.png','Pulga Animal Variante',20.00,_binary '',18,1),(10,'/categorias/pulga_animal3.png','Pulga Animal Edición',22.00,_binary '',15,1),(11,'/categorias/set_frutal.png','Set Frutal Completo',120.00,_binary '',6,2),(12,'/categorias/manzana.png','Manzana roja',25.00,_binary '',20,2),(13,'/categorias/platano.png','Plátano amarillo',22.00,_binary '',25,2),(14,'/categorias/uvas.png','Uvas moradas',35.00,_binary '',15,2),(15,'/categorias/naranja.png','Naranja jugosa',20.00,_binary '',30,2),(16,'/categorias/fresa.png','Fresa dulce',18.00,_binary '',35,2),(17,'/categorias/pina.png','Piña tropical',40.00,_binary '',12,2),(18,'/categorias/sandia.png','Sandía fresca',45.00,_binary '',10,2),(19,'/categorias/limon.png','Limón verde',15.00,_binary '',40,2),(20,'/categorias/cereza.png','Cereza roja',28.00,_binary '',22,2),(21,'/categorias/brujas.png','Bruja Piruja',400.00,_binary '',5,3),(22,'/categorias/jarrones.png','Jarrón floral',85.00,_binary '',8,3),(23,'/categorias/arbol_vida.png','Árbol de la vida',150.00,_binary '',6,3),(24,'/categorias/casa_campo.png','Casa de campo',120.00,_binary '',7,3),(25,'/categorias/fuente.png','Fuente decorativa',200.00,_binary '',4,3),(26,'/categorias/sol.png','Sol radiante',75.00,_binary '',12,3),(27,'/categorias/luna.png','Luna creciente',75.00,_binary '',12,3),(28,'/categorias/estrella.png','Estrella fugaz',65.00,_binary '',15,3),(29,'/categorias/corazon.png','Corazón enamorado',55.00,_binary '',18,3),(30,'/categorias/mariposa.png','Mariposa colorida',48.00,_binary '',20,3);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id_role` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id_role`),
  UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (2,'ADMIN'),(1,'CLIENTE');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `size`
--

DROP TABLE IF EXISTS `size`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `size` (
  `id_size` bigint NOT NULL AUTO_INCREMENT,
  `dimension` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id_size`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `size`
--

LOCK TABLES `size` WRITE;
/*!40000 ALTER TABLE `size` DISABLE KEYS */;
INSERT INTO `size` VALUES (1,'1.5x1.5 cm','Super Small'),(2,'2x2 cm','Pulga'),(3,'3x3 cm','Small'),(4,'5x5 cm','Medium'),(5,'8x8 cm','Large');
/*!40000 ALTER TABLE `size` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id_user` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `id_persona` bigint DEFAULT NULL,
  `id_role` bigint DEFAULT NULL,
  `auth_provider` varchar(255) DEFAULT NULL,
  `google_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK58t4q8mdd9y6kwcbosxbs1ycd` (`id_persona`),
  UNIQUE KEY `UKovh8xmu9ac27t18m56gri58i1` (`google_id`),
  KEY `FKt92dgi4412ywy3u8tm9jwdya5` (`id_role`),
  CONSTRAINT `FKl6yj3pslomlljh0ob6hfqqsvd` FOREIGN KEY (`id_persona`) REFERENCES `personas` (`id_persona`),
  CONSTRAINT `FKt92dgi4412ywy3u8tm9jwdya5` FOREIGN KEY (`id_role`) REFERENCES `roles` (`id_role`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'AireSeco@hotmail.com','$2a$10$CsDnr36SMX.LzN2ZR832g.WHqxyUjFjG6icCB0MMNSn7FSv98LG.a',1,2,'LOCAL',NULL),(2,'jairvillaltav08@gmail.com','$2a$10$JIRB9ROb79oW39xpKVJ2vutZCydNmThOPCTbE2Vn3fpA91AeaFGIC',2,1,'GOOGLE','100508594506152075623');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
