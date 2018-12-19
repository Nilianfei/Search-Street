/*
Navicat MySQL Data Transfer

Source Server         : root
Source Server Version : 50723
Source Host           : localhost:3306
Source Database       : search_street

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2018-12-19 21:38:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_person_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_person_info`;
CREATE TABLE `tb_person_info` (
  `user_id` int(10) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(32) DEFAULT NULL,
  `profile_img` varchar(1024) DEFAULT NULL,
  `email` varchar(1024) DEFAULT NULL,
  `sex` varchar(2) DEFAULT NULL,
  `age` int(3) unsigned DEFAULT '0',
  `phone` varchar(15) DEFAULT NULL,
  `sou_coin` int(10) unsigned DEFAULT '0',
  `user_type` int(2) NOT NULL DEFAULT '0' COMMENT '0:普通用户，1:管理员',
  `enable_status` int(2) NOT NULL DEFAULT '1' COMMENT '0:禁止使用搜街，1:允许使用搜街',
  `create_time` datetime DEFAULT NULL,
  `last_edit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
