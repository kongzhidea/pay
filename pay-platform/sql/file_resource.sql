/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50620
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2016-09-06 19:56:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for file_resource
-- ----------------------------
DROP TABLE IF EXISTS `file_resource`;
CREATE TABLE `file_resource` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `file_id` varchar(32) NOT NULL DEFAULT '',
  `data` mediumblob NOT NULL,
  `name` varchar(100) NOT NULL DEFAULT '',
  `size` bigint(11) NOT NULL,
  `file_type` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
