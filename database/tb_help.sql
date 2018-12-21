/*
 Navicat Premium Data Transfer

 Source Server         : summerunreal
 Source Server Type    : MySQL
 Source Server Version : 50642
 Source Host           : localhost:3306
 Source Schema         : search_street

 Target Server Type    : MySQL
 Target Server Version : 50642
 File Encoding         : 65001

 Date: 21/12/2018 21:23:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_help
-- ----------------------------
DROP TABLE IF EXISTS `tb_help`;
CREATE TABLE `tb_help`  (
  `help_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '帮助id\n',
  `appeal_id` int(10) NOT NULL COMMENT '求助id\n',
  `user_id` int(10) NOT NULL COMMENT '用户id\n',
  `help_status` int(2) NOT NULL DEFAULT 0 COMMENT '帮助状态（0求助用户未确定帮助对象，1已接受帮助，2未接受帮助，3已结束）\n',
  `completion` int(2) NULL DEFAULT NULL COMMENT '完成度评分',
  `efficiency` int(2) NULL DEFAULT NULL COMMENT '效率评分',
  `attitude` int(2) NULL DEFAULT NULL COMMENT '态度评分',
  `additional_coin` int(10) NULL DEFAULT NULL COMMENT '追赏金',
  PRIMARY KEY (`help_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '帮把手' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
