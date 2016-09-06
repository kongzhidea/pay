/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50620
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2016-09-06 19:56:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for pay_channel
-- ----------------------------
DROP TABLE IF EXISTS `pay_channel`;
CREATE TABLE `pay_channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pay_type_id` int(11) NOT NULL,
  `pay_type_name` varchar(255) DEFAULT NULL,
  `pay_type_code` varchar(255) DEFAULT NULL,
  `pay_channel_name` varchar(255) DEFAULT NULL,
  `sign_type` varchar(255) NOT NULL,
  `cert_file_id` varchar(32) DEFAULT NULL,
  `api_key` varchar(255) DEFAULT NULL,
  `app_id` varchar(255) NOT NULL,
  `mch_id` varchar(255) NOT NULL,
  `status` smallint(6) NOT NULL,
  `mch_key` varchar(1024) DEFAULT NULL,
  `platform_key` varchar(1024) DEFAULT NULL,
  `query_channel_id` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
