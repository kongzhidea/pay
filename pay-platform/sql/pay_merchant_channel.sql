/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50620
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2016-09-06 19:56:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for pay_merchant_channel
-- ----------------------------
DROP TABLE IF EXISTS `pay_merchant_channel`;
CREATE TABLE `pay_merchant_channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pay_type_id` int(11) DEFAULT NULL,
  `pay_merchant_id` int(11) DEFAULT NULL,
  `pay_channel_id` int(11) NOT NULL,
  `trade_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pay_merchant_id` (`pay_merchant_id`),
  KEY `pay_channel_id` (`pay_channel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;
