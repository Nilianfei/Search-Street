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

 Date: 26/03/2019 17:35:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE `tb_order`  (
  `order_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `service_id` int(10) NOT NULL COMMENT '服务id',
  `user_id` int(10) NOT NULL COMMENT '用户id',
  `service_count` int(10) NOT NULL DEFAULT 1 COMMENT '服务数量',
  `order_status` int(2) NOT NULL DEFAULT 0 COMMENT '订单状态（0已下单，1待评价，2已完成,3已取消）',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '订单创建时间',
  `over_time` datetime(0) NULL DEFAULT NULL COMMENT '订单结束时间',
  `service_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务名称',
  `order_price` double NOT NULL COMMENT '订单价格',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tb_order
-- ----------------------------
INSERT INTO `tb_order` VALUES (5, 6, 1, 1, 0, '2018-11-25 17:52:09', NULL, '茄子肉沫', 15);
INSERT INTO `tb_order` VALUES (13, 7, 1, 1, 2, '2019-03-25 18:31:51', '2019-03-27 09:57:46', '123', 12);
INSERT INTO `tb_order` VALUES (14, 7, 1, 2, 2, '2019-03-26 02:32:30', '2019-03-27 17:57:46', '123', 42);
INSERT INTO `tb_order` VALUES (15, 6, 1, 1, 3, '2019-03-26 02:33:41', NULL, '茄子肉沫', 15);
INSERT INTO `tb_order` VALUES (16, 6, 1, 2, 1, '2019-03-23 17:57:46', '2019-03-25 17:57:46', '茄子肉沫', 30);
INSERT INTO `tb_order` VALUES (17, 1, 1, 1, 3, '2019-03-26 02:33:41', NULL, '测试service店铺', 12);
INSERT INTO `tb_order` VALUES (18, 7, 1, 1, 2, '2019-03-25 18:31:51', '2019-03-27 09:57:46', '123', 12);
INSERT INTO `tb_order` VALUES (19, 7, 1, 2, 2, '2019-03-26 02:32:30', '2019-03-27 17:57:46', '123', 42);
INSERT INTO `tb_order` VALUES (20, 6, 1, 1, 3, '2019-03-26 02:33:41', NULL, '茄子肉沫', 15);
INSERT INTO `tb_order` VALUES (21, 6, 1, 2, 1, '2019-03-23 17:57:46', '2019-03-25 17:57:46', '茄子肉沫', 30);
INSERT INTO `tb_order` VALUES (22, 1, 1, 1, 3, '2019-03-26 02:33:41', NULL, '测试service店铺', 12);
INSERT INTO `tb_order` VALUES (23, 7, 1, 1, 2, '2019-03-25 18:31:51', '2019-03-27 09:57:46', '123', 12);
INSERT INTO `tb_order` VALUES (24, 7, 1, 2, 2, '2019-03-26 02:32:30', '2019-03-27 17:57:46', '123', 42);
INSERT INTO `tb_order` VALUES (25, 6, 1, 1, 3, '2019-03-26 02:33:41', NULL, '茄子肉沫', 15);
INSERT INTO `tb_order` VALUES (26, 6, 1, 2, 1, '2019-03-23 17:57:46', '2019-03-25 17:57:46', '茄子肉沫', 30);
INSERT INTO `tb_order` VALUES (27, 1, 1, 1, 3, '2019-03-26 02:33:41', NULL, '测试service店铺', 12);

SET FOREIGN_KEY_CHECKS = 1;
