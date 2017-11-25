/*
Navicat MySQL Data Transfer

Source Server         : jfsc-119.28.44.192
Source Server Version : 50719
Source Host           : 119.28.44.192:3306
Source Database       : eth

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-09-26 17:27:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `block`
-- ----------------------------
DROP TABLE IF EXISTS `block`;
CREATE TABLE `block` (
  `id` int(11) NOT NULL,
  `hash` varchar(100) DEFAULT NULL,
  `number` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of block
-- ----------------------------

-- ----------------------------
-- Table structure for `transaction`
-- ----------------------------
DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction` (
  `id` int(11) NOT NULL,
  `block_number` int(11) DEFAULT NULL,
  `transaction_index` int(11) DEFAULT NULL,
  `hash` varchar(500) NOT NULL,
  `time_stamp` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of transaction
-- ----------------------------
