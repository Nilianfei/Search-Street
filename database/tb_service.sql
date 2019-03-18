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

 Date: 19/03/2019 01:14:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_service
-- ----------------------------
DROP TABLE IF EXISTS `tb_service`;
CREATE TABLE `tb_service`  (
  `service_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '服务id',
  `shop_id` int(10) NOT NULL COMMENT '店铺id',
  `service_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务名称',
  `service_price` int(10) NOT NULL COMMENT '服务价格',
  `service_priority` int(2) NOT NULL COMMENT '优先级',
  `service_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务描述',
  `service_content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务内容',
  `service_img_addr` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务相关图片',
  PRIMARY KEY (`service_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '服务表单' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tb_service
-- ----------------------------
INSERT INTO `tb_service` VALUES (1, 2, '测试service店铺', 12, 5, '提供各式果汁和奶茶饮品', '饮品', '\\upload\\images\\item\\shop\\serviceImg\\1\\2019031823550876447.png');
INSERT INTO `tb_service` VALUES (3, 3, '测试service店铺3', 12, 2, NULL, NULL, NULL);
INSERT INTO `tb_service` VALUES (4, 3, '测试service店铺4', 12, 3, NULL, NULL, NULL);
INSERT INTO `tb_service` VALUES (5, 2, '理发', 12, 2, '提供洗剪吹服务', '理发', '');
INSERT INTO `tb_service` VALUES (6, 1, '茄子肉沫', 15, 2, '优质茄子经过油炸，和瘦肉精致翻炒制成', '茄子，瘦肉', NULL);
INSERT INTO `tb_service` VALUES (7, 1, '123', 21, 2, '123456', '213', NULL);

SET FOREIGN_KEY_CHECKS = 1;
