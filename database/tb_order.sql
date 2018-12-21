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

 Date: 21/12/2018 20:52:46
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
  `service_count` int(10) NOT NULL COMMENT '服务数量',
  `order_status` int(2) NOT NULL DEFAULT 0 COMMENT '订单状态（0已下单，1完成订单，2已取消订单,3已删除）',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '订单创建时间',
  `over_time` datetime(0) NULL DEFAULT NULL COMMENT '订单结束时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
