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

 Date: 03/01/2019 17:49:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
