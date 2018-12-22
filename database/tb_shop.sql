/*
Navicat MySQL Data Transfer

Source Server         : root
Source Server Version : 50723
Source Host           : localhost:3306
Source Database       : search_street

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2018-12-22 10:49:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_shop
-- ----------------------------
DROP TABLE IF EXISTS `tb_shop`;
CREATE TABLE `tb_shop` (
  `shop_id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `shop_name` varchar(64) NOT NULL,
  `shop_img` varchar(1024) DEFAULT NULL,
  `business_license_code` varchar(128) DEFAULT NULL,
  `business_license_img` varchar(1024) DEFAULT NULL,
  `phone` varchar(15) NOT NULL,
  `province` varchar(64) NOT NULL,
  `city` varchar(64) NOT NULL,
  `district` varchar(64) NOT NULL,
  `full_address` varchar(255) NOT NULL,
  `shop_more_info` varchar(255) DEFAULT NULL,
  `is_mobile` int(2) NOT NULL DEFAULT '1',
  `open_time` datetime DEFAULT NULL,
  `close_time` datetime DEFAULT NULL,
  `profile_img` varchar(1024) NOT NULL,
  `coordinate_x` float NOT NULL,
  `coordinate_y` float NOT NULL,
  `enable_status` int(2) NOT NULL DEFAULT '0',
  `business_scope` varchar(128) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
