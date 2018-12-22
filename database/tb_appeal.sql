/*
Navicat MySQL Data Transfer

Source Server         : root
Source Server Version : 50723
Source Host           : localhost:3306
Source Database       : search_street

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2018-12-22 10:56:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_appeal
-- ----------------------------
DROP TABLE IF EXISTS `tb_appeal`;
CREATE TABLE `tb_appeal` (
  `appeal_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '求助id',
  `user_id` int(10) NOT NULL COMMENT '用户id',
  `appeal_img` varchar(1024) DEFAULT NULL COMMENT '求助相关图片',
  `appeal_title` varchar(32) NOT NULL COMMENT '求助标题',
  `appeal_desc` varchar(128) NOT NULL COMMENT '求助描述',
  `appeal_content` varchar(512) NOT NULL COMMENT '求助内容',
  `province` varchar(32) NOT NULL COMMENT '省份',
  `city` varchar(32) NOT NULL COMMENT '城市',
  `district` varchar(32) NOT NULL COMMENT '地区',
  `full_address` varchar(200) NOT NULL COMMENT '详细地址',
  `sou_coin` int(10) NOT NULL COMMENT '回报的搜币',
  `appeal_status` int(2) NOT NULL DEFAULT '0' COMMENT '求助的状态（0不确定帮助对象，1已确定帮助对象，2已完成,3已删除）',
  `coordinate_x` float NOT NULL COMMENT '定位的纬度',
  `coordinate_y` float NOT NULL COMMENT '定位的经度',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  PRIMARY KEY (`appeal_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='寻帮助';
