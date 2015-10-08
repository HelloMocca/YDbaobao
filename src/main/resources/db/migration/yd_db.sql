-- MySQL dump 10.13  Distrib 5.6.25, for osx10.8 (x86_64)
--
-- Host: localhost    Database: ydbaobao
-- ------------------------------------------------------

--
-- Table structure for table `ADMINCONFIG`
--


DROP TABLE IF EXISTS `ADMINCONFIG`;

CREATE TABLE ADMINCONFIG (
  adminConfigId int(11) unsigned NOT NULL AUTO_INCREMENT,
  adminDisplayProducts int(11) DEFAULT '16',
  adminPassword varchar(20) COLLATE utf8_general_ci DEFAULT '1111',
  adminCostPerWeight int(11) NOT NULL DEFAULT '7300',
  PRIMARY KEY (adminConfigId)
);

INSERT INTO ADMINCONFIG VALUES (0, 16, 1111, 7300);

--
-- Table structure for table `BRANDS`
--

DROP TABLE IF EXISTS `BRANDS`;
CREATE TABLE `BRANDS` (
  `brandId` int(11) NOT NULL AUTO_INCREMENT,
  `brandName` varchar(50) COLLATE utf8_general_ci NOT NULL,
  `brandCount` int(11) DEFAULT '0',
  `discount_1` int(8) DEFAULT '0',
  `discount_2` int(8) DEFAULT '0',
  `discount_3` int(8) DEFAULT '0',
  `discount_4` int(8) DEFAULT '0',
  `discount_5` int(8) DEFAULT '0',
  `brandSize` varchar(24) COLLATE utf8_general_ci NOT NULL DEFAULT 'FREE',
  PRIMARY KEY (`brandId`)
);

--
-- Table structure for table `CATEGORY`
--

DROP TABLE IF EXISTS `CATEGORY`;
CREATE TABLE `CATEGORY` (
  `categoryId` int(11) NOT NULL AUTO_INCREMENT,
  `categoryName` varchar(50) COLLATE utf8_general_ci NOT NULL,
  `categoryCount` int(11) DEFAULT '0',
  PRIMARY KEY (`categoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

INSERT INTO CATEGORY VALUES (DEFAULT, '상의일반', DEFAULT);
INSERT INTO CATEGORY VALUES (DEFAULT, '하의일반', DEFAULT);
INSERT INTO CATEGORY VALUES (DEFAULT, '원피스', DEFAULT);
INSERT INTO CATEGORY VALUES (DEFAULT, '스커트', DEFAULT);
INSERT INTO CATEGORY VALUES (DEFAULT, '신발', DEFAULT);
INSERT INTO CATEGORY VALUES (DEFAULT, '악세서리', DEFAULT);

--
-- Table structure for table `CUSTOMERS`
--

DROP TABLE IF EXISTS `CUSTOMERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CUSTOMERS` (
  `customerId` varchar(50) COLLATE utf8_general_ci NOT NULL,
  `customerName` varchar(50) COLLATE utf8_general_ci NOT NULL,
  `customerPassword` varchar(50) COLLATE utf8_general_ci NOT NULL,
  customerGrade int(5) NOT NULL DEFAULT '0',
  `customerPhone` varchar(50) COLLATE utf8_general_ci NOT NULL,
  `customerEmail` varchar(50) COLLATE utf8_general_ci DEFAULT NULL,
  `customerAddress` varchar(100) COLLATE utf8_general_ci DEFAULT NULL,
  `customerCreateDate` datetime DEFAULT NULL,
  `customerUpdateDate` datetime DEFAULT NULL,
  PRIMARY KEY (`customerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

INSERT INTO CUSTOMERS VALUES('test', 'tester', 'test1234', 0, '010-0000-0000', 'yd@ydbaobao.cafe24.com', 'KOREA REPUBLIC OF','2015-09-09', '2015-09-09');

--
-- Table structure for table `INDEXIMAGES`
--

DROP TABLE IF EXISTS `INDEXIMAGES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `INDEXIMAGES` (
  `indexImageId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `indexImageName` varchar(50) COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`indexImageId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ITEMS`
--

DROP TABLE IF EXISTS `ITEMS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE ITEMS (
  itemId int(11) NOT NULL AUTO_INCREMENT,
  customerId varchar(50) COLLATE utf8_general_ci DEFAULT NULL,
  productId int(11) DEFAULT NULL,
  itemStatus varchar(50) COLLATE utf8_general_ci DEFAULT 'I',
  price int(11) DEFAULT NULL,
  orderId int(11) DEFAULT NULL,
  PRIMARY KEY (itemId),
  CONSTRAINT `items_ibfk_1` FOREIGN KEY (`customerId`) REFERENCES `CUSTOMERS` (`customerId`) ON DELETE CASCADE,
  CONSTRAINT `items_ibfk_3` FOREIGN KEY (`productId`) REFERENCES `PRODUCTS` (`productId`) ON DELETE CASCADE,
  FOREIGN KEY (orderId) REFERENCES ORDERS (orderId) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PRODUCTIMAGES`
--

DROP TABLE IF EXISTS `PRODUCTIMAGES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PRODUCTIMAGES` (
  `imageId` int(11) NOT NULL AUTO_INCREMENT,
  `productId` int(11) NOT NULL,
  `imageName` varchar(200) COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`imageId`),
  KEY `productId` (`productId`),
  CONSTRAINT `productimages_ibfk_1` FOREIGN KEY (`productId`) REFERENCES `PRODUCTS` (`productId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PRODUCTS`
DROP TABLE IF EXISTS `PRODUCTS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PRODUCTS` (
  `productId` int(11) NOT NULL AUTO_INCREMENT,
  `productName` varchar(50) COLLATE utf8_general_ci DEFAULT NULL,
  `categoryId` int(11) DEFAULT '0',
  `brandId` int(11) DEFAULT NULL,
  `productPrice` int(11) NOT NULL DEFAULT '0',
  `productImage` varchar(200) COLLATE utf8_general_ci DEFAULT NULL,
  `productDescription` text COLLATE utf8_general_ci,
  `productCreateDate` datetime DEFAULT NULL,
  `productUpdateDate` datetime DEFAULT NULL,
  `productSize` varchar(24) COLLATE utf8_general_ci DEFAULT 'FREE',
  `isSoldout` int(1) DEFAULT '0',
  PRIMARY KEY (`productId`),
  KEY `categoryId` (`categoryId`),
  KEY `brandId` (`brandId`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`categoryId`) REFERENCES `CATEGORY` (`categoryId`),
  CONSTRAINT `products_ibfk_2` FOREIGN KEY (`brandId`) REFERENCES `BRANDS` (`brandId`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QUANTITY`
--

DROP TABLE IF EXISTS `QUANTITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE QUANTITY (
  quantityId int(11) NOT NULL AUTO_INCREMENT,
  itemId int(11) NOT NULL,
  size varchar(50) COLLATE utf8_general_ci NOT NULL,
  value int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (quantityId),
  UNIQUE KEY (itemId, size),
  FOREIGN KEY (itemId) REFERENCES ITEMS (itemId) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table 'ORDERS'
--

DROP TABLE IF EXISTS ORDERS;
CREATE TABLE ORDERS (
  orderId int(11) NOT NULL AUTO_INCREMENT,
  customerId varchar(50) NOT NULL,
  shippingCost int(11) NOT NULL DEFAULT '0',
  extraDiscount int(11) NOT NULL DEFAULT '0',
  orderPrice int(11) NOT NULL DEFAULT '0',
  paiedPrice int(11) NOT NULL DEFAULT '0',
  recallPrice int(11) NOT NULL DEFAULT '0',
  orderDate datetime NOT NULL,
  PRIMARY KEY (orderId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
