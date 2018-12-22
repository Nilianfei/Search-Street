/*
Navicat MySQL Data Transfer

Source Server         : root
Source Server Version : 50723
Source Host           : localhost:3306
Source Database       : search_street

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2018-12-22 10:50:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_shop_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_shop_comment`;
CREATE TABLE `tb_shop_comment` (
  `shop_comment_id` int(10) NOT NULL AUTO_INCREMENT,
  `shop_id` int(10) NOT NULL,
  `user_id` int(10) NOT NULL,
  `comment_content` varchar(512) DEFAULT NULL,
  `service_rating` int(3) unsigned NOT NULL,
  `star_rating` int(1) unsigned NOT NULL,
  PRIMARY KEY (`shop_comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
