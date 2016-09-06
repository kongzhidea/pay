/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50620
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2016-09-06 19:56:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for pay_order
-- ----------------------------
DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pay_type_code` varchar(50) DEFAULT NULL,
  `pay_order_no` varchar(255) NOT NULL,
  `trade_pay_no` varchar(255) DEFAULT NULL,
  `pre_pay_id` varchar(100) DEFAULT NULL,
  `pay_id` varchar(100) DEFAULT NULL,
  `user_ip` varchar(50) DEFAULT NULL,
  `pay_amount` int(11) DEFAULT NULL,
  `pay_time` datetime DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  `error_code` varchar(32) DEFAULT NULL,
  `error_msg` varchar(128) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `expire_time` datetime DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  `buyer_logon_id` varchar(255) DEFAULT NULL,
  `notify_url` varchar(255) DEFAULT NULL,
  `extra` varchar(100) DEFAULT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `detail` varchar(500) DEFAULT NULL,
  `code_url` varchar(255) DEFAULT NULL,
  `merchant_id` varchar(255) DEFAULT NULL,
  `trade_type` varchar(255) DEFAULT NULL,
  `return_url` varchar(255) DEFAULT NULL,
  `refund_amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pay_order_no` (`pay_order_no`) USING BTREE,
  KEY `trade_pay_no` (`trade_pay_no`),
  KEY `pay_id` (`pay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12478 DEFAULT CHARSET=utf8;
