/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : search_street

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 12/01/2019 22:10:42
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
  `appeal_img` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '求助相关图片',
  `appeal_title` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '求助标题',
  `appeal_desc` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '求助描述',
  `appeal_content` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '求助内容',
  `province` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '省份',
  `city` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '城市',
  `district` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地区',
  `full_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '详细地址',
  `sou_coin` int(10) NOT NULL COMMENT '回报的搜币',
  `appeal_status` int(2) NOT NULL DEFAULT 0 COMMENT '求助的状态（0不确定帮助对象，1已确定帮助对象，2已完成,3已删除）',
  `coordinate_x` float NOT NULL COMMENT '定位的纬度',
  `coordinate_y` float NOT NULL COMMENT '定位的经度',
  `start_time` datetime(0) NOT NULL COMMENT '开始时间',
  `end_time` datetime(0) NOT NULL COMMENT '结束时间',
  PRIMARY KEY (`appeal_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '寻帮助' ROW_FORMAT = Compact;

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

-- ----------------------------
-- Table structure for tb_local_auth
-- ----------------------------
DROP TABLE IF EXISTS `tb_local_auth`;
CREATE TABLE `tb_local_auth`  (
  `local_auth_id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `user_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `last_edit_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`local_auth_id`) USING BTREE,
  UNIQUE INDEX `uk_local_profile`(`user_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for tb_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE `tb_order`  (
  `order_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `service_id` int(10) NOT NULL COMMENT '服务id',
  `user_id` int(10) NOT NULL COMMENT '用户id',
  `service_count` int(10) NOT NULL COMMENT '服务数量',
  `order_status` int(2) NOT NULL DEFAULT 0 COMMENT '订单状态（0已下单，1完成订单，2已取消订单,3已删除）',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '订单创建时间',
  `over_time` datetime(0) NULL DEFAULT NULL COMMENT '订单结束时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for tb_person_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_person_info`;
CREATE TABLE `tb_person_info`  (
  `user_id` int(10) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `profile_img` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `birth` date NULL DEFAULT NULL,
  `phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sou_coin` int(10) UNSIGNED NULL DEFAULT 0,
  `user_type` int(2) NOT NULL DEFAULT 0 COMMENT '0:普通用户，1:管理员',
  `enable_status` int(2) NOT NULL DEFAULT 1 COMMENT '0:禁止使用搜街，1:允许使用搜街',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `last_edit_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for tb_service
-- ----------------------------
DROP TABLE IF EXISTS `tb_service`;
CREATE TABLE `tb_service`  (
  `service_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '服务id',
  `shop_id` int(10) NOT NULL COMMENT '店铺id',
  `service_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务名称',
  `service_price` int(10) NOT NULL COMMENT '服务价格',
  `service_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务描述',
  `service_content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务内容',
  `service_img` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务相关图片',
  PRIMARY KEY (`service_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '服务表单' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for tb_shop
-- ----------------------------
DROP TABLE IF EXISTS `tb_shop`;
CREATE TABLE `tb_shop`  (
  `shop_id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `shop_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `business_license_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `business_license_img` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `per_cost` int(10) NULL DEFAULT 0,
  `phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `province` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `city` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `district` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `full_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `shop_more_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_mobile` int(2) NOT NULL DEFAULT 1,
  `open_time` time(0) NULL DEFAULT NULL,
  `close_time` time(0) NULL DEFAULT NULL,
  `profile_img` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `coordinate_x` float NOT NULL,
  `coordinate_y` float NOT NULL,
  `enable_status` int(2) NOT NULL DEFAULT 0,
  `business_scope` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `last_edit_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`shop_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_shop_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_shop_comment`;
CREATE TABLE `tb_shop_comment`  (
  `shop_comment_id` int(10) NOT NULL AUTO_INCREMENT,
  `shop_id` int(10) NOT NULL,
  `user_id` int(10) NOT NULL,
  `comment_content` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `service_rating` int(3) UNSIGNED NOT NULL,
  `star_rating` int(1) UNSIGNED NOT NULL,
  PRIMARY KEY (`shop_comment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_shop_img
-- ----------------------------
DROP TABLE IF EXISTS `tb_shop_img`;
CREATE TABLE `tb_shop_img`  (
  `shop_img_id` int(20) NOT NULL AUTO_INCREMENT,
  `img_addr` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `shop_id` int(10) NULL DEFAULT NULL,
  PRIMARY KEY (`shop_img_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_wechat_auth
-- ----------------------------
DROP TABLE IF EXISTS `tb_wechat_auth`;
CREATE TABLE `tb_wechat_auth`  (
  `wechat_auth_id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `open_id` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`wechat_auth_id`) USING BTREE,
  UNIQUE INDEX `uk_wechat_profile`(`open_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
