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

 Date: 19/03/2019 01:14:46
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
  `order_status` int(2) NOT NULL DEFAULT 0 COMMENT '订单状态（0已下单，1未完成，2已取消,3待评价，4已完成）',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '订单创建时间',
  `over_time` datetime(0) NULL DEFAULT NULL COMMENT '订单结束时间',
  `service_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务名称',
  `tb_ordercol` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
