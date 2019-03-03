/*
 Navicat MySQL Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost:3306
 Source Schema         : search_street

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 03/03/2019 23:01:26
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
  `appeal_title` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '求助标题',
  `user_id` int(10) NOT NULL COMMENT '用户id\n',
  `help_status` int(2) NOT NULL DEFAULT 0 COMMENT '帮助状态（0求助用户未确定帮助对象，1已接受帮助，2未接受帮助，3已结束）\n',
  `completion` int(2) UNSIGNED NOT NULL DEFAULT 0 COMMENT '完成度评分',
  `efficiency` int(2) UNSIGNED NOT NULL DEFAULT 0 COMMENT '效率评分',
  `attitude` int(2) UNSIGNED NOT NULL DEFAULT 0 COMMENT '态度评分',
  `avg_completion` float(2, 1) UNSIGNED NOT NULL DEFAULT 0.0 COMMENT '之前的平均完成度评分',
  `avg_efficiency` float(2, 1) UNSIGNED NOT NULL DEFAULT 0.0 COMMENT '之前的平均效率评分',
  `avg_attitude` float(2, 1) UNSIGNED NOT NULL DEFAULT 0.0 COMMENT '之前的平均态度评分',
  `additional_coin` int(10) UNSIGNED NOT NULL COMMENT '追赏金',
  `end_time` datetime(0) NOT NULL,
  PRIMARY KEY (`help_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '帮把手' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
