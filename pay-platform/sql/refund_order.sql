/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50620
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50620
File Encoding         : 65001

Date: 2016-09-06 19:56:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for refund_order
-- ----------------------------
DROP TABLE IF EXISTS `refund_order`;
CREATE TABLE `refund_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pay_channel_id` int(255) DEFAULT NULL,
  `pay_type_code` varchar(255) DEFAULT NULL,
  `pay_order_no` varchar(255) DEFAULT NULL,
  `trade_pay_no` varchar(255) DEFAULT NULL,
  `refund_order_no` varchar(255) DEFAULT NULL,
  `trade_refund_no` varchar(255) DEFAULT NULL,
  `pay_id` varchar(255) DEFAULT NULL,
  `pay_amount` int(11) DEFAULT NULL,
  `refund_id` varchar(255) DEFAULT NULL,
  `refund_amount` int(11) DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  `error_code` varchar(255) DEFAULT NULL,
  `error_msg` varchar(255) DEFAULT NULL,
  `refund_time` datetime DEFAULT NULL,
  `merchant_id` varchar(255) DEFAULT NULL,
  `trade_type` varchar(255) DEFAULT NULL,
  `notify_url` varchar(255) DEFAULT NULL,
  `refund_reason` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `refund_order_no` (`refund_order_no`),
  UNIQUE KEY `trade_refund_no_mid` (`trade_refund_no`,`merchant_id`) USING BTREE,
  KEY `trade_pay_no` (`trade_pay_no`),
  KEY `pay_order_no` (`pay_order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=246 DEFAULT CHARSET=utf8;
