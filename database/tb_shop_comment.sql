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

 Date: 26/03/2019 17:36:11
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
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tb_shop_comment
-- ----------------------------
INSERT INTO `tb_shop_comment` VALUES (5, 2, 5, 1, '55555', 100, 5, NULL);
INSERT INTO `tb_shop_comment` VALUES (13, 2, 13, 1, '6666666699', 80, 3, NULL);
INSERT INTO `tb_shop_comment` VALUES (14, 1, 14, 1, '0', 100, 5, NULL);
INSERT INTO `tb_shop_comment` VALUES (15, 1, 15, 1, '5454545454', 100, 5, NULL);
INSERT INTO `tb_shop_comment` VALUES (16, 1, 16, 1, '123548888', 80, 4, NULL);
INSERT INTO `tb_shop_comment` VALUES (17, 1, 17, 1, '5555555555', 100, 4, NULL);
INSERT INTO `tb_shop_comment` VALUES (18, 1, 18, 1, 'string222', 95, 5, NULL);
INSERT INTO `tb_shop_comment` VALUES (28, 1, 19, 1, '666666665656565', 60, 5, NULL);
INSERT INTO `tb_shop_comment` VALUES (29, 1, 20, 1, '54646454656', 80, 5, NULL);
INSERT INTO `tb_shop_comment` VALUES (30, 1, 21, 1, '5555551111', 60, 5, NULL);
INSERT INTO `tb_shop_comment` VALUES (31, 1, 22, 1, '2131231231', 80, 5, NULL);
INSERT INTO `tb_shop_comment` VALUES (32, 1, 23, 1, '5646464564', 80, 5, NULL);

SET FOREIGN_KEY_CHECKS = 1;
