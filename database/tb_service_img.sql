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

 Date: 19/03/2019 01:15:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_service_img
-- ----------------------------
DROP TABLE IF EXISTS `tb_service_img`;
CREATE TABLE `tb_service_img`  (
  `service_img_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '服务图片Id',
  `img_addr` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片地址',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '图片上传时间',
  `service_id` int(10) NOT NULL COMMENT '服务id',
  PRIMARY KEY (`service_img_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tb_service_img
-- ----------------------------
INSERT INTO `tb_service_img` VALUES (11, 'E:/SearchStreet/2.png', '2019-02-26 02:05:19', 2);
INSERT INTO `tb_service_img` VALUES (12, 'E:/SearchStreet/3.png', '2019-02-26 02:06:23', 3);
INSERT INTO `tb_service_img` VALUES (17, '\\upload\\images\\item\\shop\\serviceImg\\1\\2019031823550876447.png', '2019-03-18 23:55:08', 1);

SET FOREIGN_KEY_CHECKS = 1;
