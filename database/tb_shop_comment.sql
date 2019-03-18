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

 Date: 19/03/2019 01:15:15
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
  `order_id` int(10) NOT NULL COMMENT '订单id',
  `user_id` int(10) NOT NULL,
  `comment_content` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论',
  `service_rating` int(3) UNSIGNED NOT NULL COMMENT '服务评分',
  `star_rating` int(1) UNSIGNED NOT NULL COMMENT '星级评分',
  `comment_reply` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家回复',
  PRIMARY KEY (`shop_comment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tb_shop_comment
-- ----------------------------
INSERT INTO `tb_shop_comment` VALUES (1, 1, 1, 1, '测试shopComment内容', 100, 5, NULL);
INSERT INTO `tb_shop_comment` VALUES (3, 2, 3, 1, '测试shopComment内容3', 100, 4, NULL);
INSERT INTO `tb_shop_comment` VALUES (4, 1, 4, 2, 'string', 100, 5, NULL);
INSERT INTO `tb_shop_comment` VALUES (5, 2, 5, 1, '55555', 100, 5, NULL);
INSERT INTO `tb_shop_comment` VALUES (6, 2, 6, 1, '5555', 85, 3, NULL);
INSERT INTO `tb_shop_comment` VALUES (7, 2, 7, 1, '80', 100, 4, NULL);
INSERT INTO `tb_shop_comment` VALUES (8, 2, 8, 1, '5555555', 90, 5, NULL);
INSERT INTO `tb_shop_comment` VALUES (9, 2, 9, 1, '5555555555', 100, 5, NULL);
INSERT INTO `tb_shop_comment` VALUES (10, 2, 11, 1, '5555555555', 100, 4, NULL);
INSERT INTO `tb_shop_comment` VALUES (11, 2, 10, 1, '66666666666666666', 100, 3, NULL);
INSERT INTO `tb_shop_comment` VALUES (12, 2, 12, 1, '66666666', 100, 5, NULL);
INSERT INTO `tb_shop_comment` VALUES (13, 2, 13, 1, '6666666699', 80, 3, NULL);

SET FOREIGN_KEY_CHECKS = 1;
