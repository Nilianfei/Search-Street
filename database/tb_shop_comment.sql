/*
 Navicat Premium Data Transfer

 Source Server         : summerunreal
 Source Server Type    : MySQL
 Source Server Version : 50642
 Source Host           : localhost:3306
 Source Schema         : searchstreet

 Target Server Type    : MySQL
 Target Server Version : 50642
 File Encoding         : 65001

 Date: 21/12/2018 20:53:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
