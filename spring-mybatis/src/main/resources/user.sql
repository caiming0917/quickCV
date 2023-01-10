/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : PostgreSQL
 Source Server Version : 120009
 Source Host           : localhost:5432
 Source Catalog        : ngsoc
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 120009
 File Encoding         : 65001

 Date: 10/01/2023 15:14:43
*/


-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS "public"."user";
CREATE TABLE "public"."user" (
  "id" int8 NOT NULL,
  "user_name" varchar(30) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "age" int4,
  "email" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "monitor_notice" jsonb
)
;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO "public"."user" VALUES (1000, '张三', 20, '123@qq.com', NULL);
INSERT INTO "public"."user" VALUES (1005, '张三', 20, '123@qq.com', '{"transNotice": {"noticeMode": {"sms": true, "lanxin": false}, "noticeSwitch": true}, "connectNotice": {"noticeMode": {"sms": true, "lanxin": false}, "noticeSwitch": true}, "exceptionNotice": {"noticeMode": {"sms": true, "lanxin": false}, "noticeSwitch": true}}');
INSERT INTO "public"."user" VALUES (1010, '张三', 20, '123@qq.com', '{"transNotice": {"noticeMode": {"sms": true, "lanxin": false}, "noticeSwitch": true}, "connectNotice": {"noticeMode": {"sms": true, "lanxin": false}, "noticeSwitch": true}, "exceptionNotice": {"noticeMode": {"sms": true, "lanxin": false}, "noticeSwitch": true}}');
INSERT INTO "public"."user" VALUES (1015, '张三', 20, '123@qq.com', '{"transNotice": {"noticeMode": {"sms": true, "lanxin": false}, "noticeSwitch": true}, "connectNotice": {"noticeMode": {"sms": true, "lanxin": false}, "noticeSwitch": true}, "exceptionNotice": {"noticeMode": {"sms": true, "lanxin": false}, "noticeSwitch": true}}');

-- ----------------------------
-- Primary Key structure for table user
-- ----------------------------
ALTER TABLE "public"."user" ADD CONSTRAINT "user_pkey" PRIMARY KEY ("id");
