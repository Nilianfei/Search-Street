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

 Date: 03/03/2019 23:01:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_appeal
-- ----------------------------
DROP TABLE IF EXISTS `tb_appeal`;
CREATE TABLE `tb_appeal`  (
  `appeal_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '求助id',
  `user_id` int(10) NOT NULL COMMENT '用户id',
  `appeal_title` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '求助标题',
  `phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '求助联系电话',
  `appeal_content` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '求助内容',
  `province` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '省份',
  `city` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '城市',
  `district` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地区',
  `full_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '详细地址',
  `appeal_more_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址补充',
  `sou_coin` int(10) UNSIGNED NOT NULL COMMENT '回报的搜币',
  `appeal_status` int(2) NOT NULL DEFAULT 0 COMMENT '求助的状态（0不确定帮助对象，1已确定帮助对象，2已完成,3已删除）',
  `latitude` double NOT NULL COMMENT '定位的纬度',
  `longitude` double NOT NULL COMMENT '定位的经度',
  `start_time` datetime(0) NOT NULL COMMENT '开始时间',
  `end_time` datetime(0) NOT NULL COMMENT '结束时间',
  PRIMARY KEY (`appeal_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '寻帮助' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
