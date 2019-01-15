
-- ----------------------------
-- 京东订单表
-- ----------------------------
CREATE TABLE `jd_order` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `shop_user_id` int(11) NOT NULL COMMENT '会员id',
  `order_status` int(3) DEFAULT '0' COMMENT '订单状态 0-初始,1-订单已取消,5-失败，9-成功',
  `third_order` varchar(32) NOT NULL DEFAULT '' COMMENT '第三方订单号（本系统的）',
  `order_key` varchar(32) DEFAULT '' COMMENT '订单编号（三方返回的）',
  `pid_nums` varchar(500) DEFAULT NULL COMMENT '商品ID_数量',
  `receiver_name` varchar(32) DEFAULT '' COMMENT '收货地址',
  `province` varchar(8) DEFAULT '' COMMENT '省, 区域信息ID',
  `city` varchar(8) DEFAULT '' COMMENT '市, 区域信息ID',
  `county` varchar(8) DEFAULT '' COMMENT '县, 区域信息ID',
  `town` varchar(8) DEFAULT '' COMMENT '乡/镇, 区域信息ID',
  `address` varchar(60) DEFAULT '' COMMENT '详细地址',
  `mobile` varchar(11) DEFAULT '' COMMENT '手机号',
  `email` varchar(32) DEFAULT '' COMMENT '收货人邮箱',
  `remarke` varchar(32) DEFAULT '' COMMENT '订单备注信息',
  `error_code` varchar(32) DEFAULT NULL COMMENT '错误状态码',
  `error_message` varchar(64) DEFAULT NULL COMMENT '错误信息',
  `response_status` varchar(8) DEFAULT NULL COMMENT 'true, false',
  `order_product_price` decimal(10,2) DEFAULT '0.00' COMMENT '产品价格',
  `order_shipment_fee` decimal(10,2) DEFAULT '0.00' COMMENT '快递费用',
  `order_total_price` decimal(10,2) DEFAULT '0.00' COMMENT '订单总金额',
  `order_split` tinyint(1) DEFAULT '0' COMMENT '订单是否被拆分 true: 被拆分为多个订单, false: 未拆分',
  `result_data` varchar(500) DEFAULT NULL COMMENT '基础数据',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0-正常 1-删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_third_order` (`third_order`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='京东订单表'

-- ----------------------------
-- 商城订单表
-- ----------------------------
CREATE TABLE `nideshop_order` (
  `id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `order_sn` varchar(32) DEFAULT '',
  `user_id` mediumint(8) unsigned DEFAULT '0',
  `order_status` int(3) unsigned DEFAULT '0' COMMENT '订单状态 0-初始 101-订单已取消  102-订单已删除 103-订单已作废201-订单已付款，等待发货 300-订单已发货 301-用户确认收货9-已完成',
  `shipping_status` tinyint(1) unsigned DEFAULT '0' COMMENT '物流状态 0未发货,1已发货,2已收货,4退货',
  `pay_status` tinyint(1) unsigned DEFAULT '0' COMMENT '支付状态 0-初始 1-成功',
  `consignee` varchar(60) DEFAULT '' COMMENT '收件人姓名',
  `address_id` int(11) NOT NULL COMMENT '平台地址id',
  `country` varchar(50) DEFAULT NULL,
  `province` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `district` varchar(50) DEFAULT NULL,
  `address` varchar(255) DEFAULT '',
  `mobile` varchar(60) DEFAULT '',
  `postscript` varchar(255) DEFAULT '',
  `shipping_id` tinyint(3) DEFAULT '0',
  `shipping_name` varchar(120) DEFAULT '',
  `pay_id` varchar(64) DEFAULT '0',
  `pay_name` varchar(120) DEFAULT '',
  `shipping_fee` decimal(10,2) DEFAULT '0.00' COMMENT '快递费用',
  `actual_price` decimal(10,2) DEFAULT '0.00' COMMENT '实际需要支付的金额',
  `integral` int(10) unsigned DEFAULT '0',
  `integral_money` decimal(10,2) DEFAULT '0.00',
  `order_price` decimal(10,2) DEFAULT '0.00' COMMENT '订单总价',
  `goods_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品总价',
  `add_time` datetime DEFAULT NULL,
  `confirm_time` datetime DEFAULT NULL COMMENT '确认收货时间',
  `pay_time` datetime DEFAULT NULL COMMENT '订单支付时间',
  `freight_price` int(10) unsigned DEFAULT '0' COMMENT '配送费用',
  `coupon_id` mediumint(8) unsigned DEFAULT '0' COMMENT '使用的优惠券id',
  `parent_id` mediumint(8) unsigned DEFAULT '0',
  `coupon_price` decimal(10,2) DEFAULT NULL,
  `callback_status` enum('true','false') DEFAULT 'true',
  `shipping_no` varchar(32) DEFAULT '' COMMENT '平台流水号',
  `full_cut_price` decimal(10,2) DEFAULT NULL,
  `order_type` varchar(8) DEFAULT NULL COMMENT '1-正常 404-支付异常',
  `pid_num` varchar(480) DEFAULT NULL COMMENT '商品_数量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_sn` (`order_sn`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `order_status` (`order_status`) USING BTREE,
  KEY `shipping_status` (`shipping_status`) USING BTREE,
  KEY `pay_status` (`pay_status`) USING BTREE,
  KEY `shipping_id` (`shipping_id`) USING BTREE,
  KEY `pay_id` (`pay_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8

-- ----------------------------
-- 订单-商品表
-- ----------------------------
CREATE TABLE `nideshop_order_goods` (
  `id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` mediumint(8) unsigned NOT NULL DEFAULT '0' COMMENT '订单Id',
  `goods_id` mediumint(8) unsigned NOT NULL DEFAULT '0' COMMENT '商品id',
  `goods_name` varchar(120) NOT NULL DEFAULT '' COMMENT '商品名称',
  `goods_sn` varchar(60) NOT NULL DEFAULT '' COMMENT '商品序列号',
  `product_id` mediumint(8) unsigned NOT NULL DEFAULT '0' COMMENT '产品Id',
  `number` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '商品数量',
  `market_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '市场价',
  `retail_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '零售价格',
  `goods_specifition_name_value` text COMMENT '商品规格详情',
  `is_real` tinyint(1) unsigned DEFAULT '0' COMMENT '虚拟商品',
  `goods_specifition_ids` varchar(255) DEFAULT '' COMMENT '商品规格Ids',
  `list_pic_url` varchar(255) DEFAULT '' COMMENT '图片链接',
  `channel` varchar(6) DEFAULT 'system' COMMENT '渠道',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `good_status` int(2) DEFAULT '0' COMMENT '订单的状态 0-初始;',
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`) USING BTREE,
  KEY `goods_id` (`goods_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8

-- ----------------------------
-- 易宝支付订单表
-- ----------------------------
CREATE TABLE `yee_trade_order` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `yee_order_no` varchar(32) NOT NULL DEFAULT '' COMMENT '交易流水号(传给易宝)',
  `trade_no` varchar(32) NOT NULL DEFAULT '' COMMENT '交易流水号(关联订单)',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `pay_status` int(1) DEFAULT '0' COMMENT '订单状态 0-初始化',
  `pay_type` int(10) DEFAULT '0' COMMENT '支付方式 0-默认,1-微信支付,2-支付宝支付,3-一键支付',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '支付总金额',
  `request_param` text COMMENT '请求参数',
  `response_msg` text COMMENT '响应结果',
  `yborderid` varchar(32) DEFAULT '' COMMENT '易宝交易流水号(支付请求成功后)',
  `msg` varchar(32) DEFAULT '' COMMENT '订单状态 init,success,error',
  `error_code` varchar(18) DEFAULT NULL COMMENT '错误状态码',
  `error_msg` varchar(64) DEFAULT NULL COMMENT '错误描述',
  `card_type` varchar(1) DEFAULT NULL COMMENT '卡类型',
  `lastno` varchar(8) DEFAULT NULL COMMENT '卡号后四位',
  `bank` varchar(64) DEFAULT NULL COMMENT '支付卡所属银行的名称',
  `bank_code` varchar(32) DEFAULT NULL COMMENT '支付卡所属银行的编码',
  `pay_amount` decimal(10,2) DEFAULT '0.00' COMMENT '实际支付金额',
  `memo` varchar(32) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_trade_no` (`trade_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='易宝支付订单表'

-- ----------------------------
-- 用户充值记录表
-- ----------------------------
CREATE TABLE `third_recharge_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
  `state` varchar(2) NOT NULL DEFAULT '0' COMMENT '状态：0-初始，1-成功，2-失败',
  `amount` decimal(16,2) DEFAULT '0.00' COMMENT '金额',
  `third_trade_no` varchar(64) NOT NULL DEFAULT '' COMMENT '三方订单号',
  `trade_no` varchar(64) NOT NULL COMMENT '系统订单号',
  `card_type` varchar(1) DEFAULT NULL COMMENT '1-会员卡;2-展期卡',
  `platform_type` varchar(64) DEFAULT NULL COMMENT '平台类型',
  `version` int(1) DEFAULT '0' COMMENT '版本号',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_third_rechard_no` (`third_trade_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户充值记录'

-- ----------------------------
-- 用户账户表
-- ----------------------------
CREATE TABLE `qz_user_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `shop_user_id` int(11) NOT NULL COMMENT '会员id',
  `amount` decimal(16,2) DEFAULT '0.00' COMMENT '用户余额',
  `lock_amount` decimal(16,2) DEFAULT '0.00' COMMENT '冻结金额',
  `last_update_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_shop_user_id` (`shop_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户账户'

-- ----------------------------
-- 用户充值记录
-- ----------------------------
CREATE TABLE `qz_recharge_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `shop_user_id` int(11) NOT NULL COMMENT '会员id',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
  `state` varchar(2) NOT NULL DEFAULT '0' COMMENT '状态：0-初始，1-通过，2-拒绝',
  `operate_id` bigint(20) DEFAULT NULL COMMENT '操作人id',
  `operate_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '审核时间',
  `amount` decimal(16,2) DEFAULT '0.00' COMMENT '金额',
  `memo` varchar(128) DEFAULT NULL COMMENT '备注',
  `trade_no` varchar(64) NOT NULL COMMENT '流水号',
  `recharge_type` int(1) DEFAULT NULL COMMENT '充值类型 1-后台充值 2-速有钱充值',
  `audit_id` bigint(20) DEFAULT NULL COMMENT '审核人id',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_shop_user_id` (`shop_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户充值记录'

-- ----------------------------
-- 用户资金流水
-- ----------------------------
CREATE TABLE `qz_money_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `shop_user_id` int(11) NOT NULL COMMENT '会员id',
  `tran_type` varchar(10) NOT NULL COMMENT '资金变动类型 1-充值 2-克拉币 3-回滚/扣减冻结克拉币',
  `tran_flag` tinyint(1) NOT NULL COMMENT '金额变动标志 0-支出 1-收入',
  `tarn_amount` decimal(16,2) NOT NULL DEFAULT '0.00' COMMENT '变动金额',
  `current_amount` decimal(16,2) NOT NULL DEFAULT '0.00' COMMENT '当前余额',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `trade_no` varchar(64) NOT NULL COMMENT '交易流水号(关联各资金订单)',
  `remark` varchar(64) DEFAULT '' COMMENT '备注',
  `lock_amount` decimal(16,2) DEFAULT '0.00' COMMENT '冻结金额',
  PRIMARY KEY (`id`),
  KEY `index_shop_user_id` (`shop_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户资金流水'

-- ----------------------------
-- 9.表: sys_config 系统配置信息表
-- ----------------------------
CREATE TABLE `sys_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `key` varchar(50) DEFAULT NULL COMMENT 'key',
  `value` varchar(2000) DEFAULT NULL COMMENT 'value',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置信息表'

-- ----------------------------
-- 10.表: nideshop_sms_log  短信日志表
-- ----------------------------
CREATE TABLE `nideshop_sms_log` (
  `id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) DEFAULT NULL,
  `phone` varchar(32) DEFAULT NULL,
  `log_date` bigint(20) DEFAULT NULL,
  `sms_code` varchar(12) DEFAULT NULL,
  `send_status` bigint(20) DEFAULT NULL,
  `sms_text` varchar(255) DEFAULT NULL COMMENT '1成功 0失败',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

-- ----------------------------
-- 11.表: sys_sms_log 系统短信日志
-- ----------------------------
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志'