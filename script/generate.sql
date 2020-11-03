
-- use TableInitConfig.java to create tables below automatically
CREATE TABLE IF NOT EXISTS tb_front (
  front_id int(11) NOT NULL COMMENT '前置编号',
  chain_id int(11) NOT NULL COMMENT '链编号',
  front_ip varchar(16) NOT NULL COMMENT '前置服务ip',
  front_port int(11) DEFAULT NULL COMMENT '前置服务端口',
  memory_total_size varchar(16) NOT NULL COMMENT '内存总量（单位：KB）',
  memory_used_size varchar(16) NOT NULL COMMENT '内存使用量（单位：KB）',
  cpu_size varchar(16) NOT NULL COMMENT 'CPU的大小（单位：MHz）',
  cpu_amount varchar(4) NOT NULL COMMENT 'CPU的核数（单位：个）',
  disk_total_size varchar(16) NOT NULL COMMENT '文件系统总量（单位：KB）',
  disk_used_size varchar(16) NOT NULL COMMENT '文件系统已使用量（单位：KB）',
  description varchar(1024) COMMENT '描述',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  modify_time datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (front_id),
  UNIQUE unique_chain_front (front_id, chain_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='前置信息表';

CREATE TABLE IF NOT EXISTS tb_group (
  front_id int(11) NOT NULL COMMENT '前置编号',
  chain_id int(11) NOT NULL COMMENT '链编号',
  group_id int(11) NOT NULL COMMENT '群组编号',
  description varchar(256) COMMENT '描述',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  modify_time datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (front_id,group_id)
) ENGINE=InnoDB AUTO_INCREMENT=200001 DEFAULT CHARSET=utf8 COMMENT='群组信息表';

CREATE TABLE IF NOT EXISTS tb_group_basic_data (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '自增编号',
  front_id int(11) NOT NULL COMMENT '前置编号',
  chain_id int(11) NOT NULL COMMENT '链编号',
  group_id int(11) NOT NULL COMMENT '群组编号',
  size bigint NOT NULL COMMENT '群组物理大小',
  trans_count bigint NOT NULL COMMENT '群组交易量',
  comment varchar(256) COMMENT '备注',
  record_month int(11) NOT NULL COMMENT '统计时间，年份加月份，如202008',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  modify_time datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (id,record_month),
  INDEX index_front_chain (front_id,chain_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='群组基本数据表'
  PARTITION BY RANGE (record_month) (
  PARTITION p_default VALUES LESS THAN MAXVALUE
);

CREATE TABLE IF NOT EXISTS tb_node_monitor (
		id bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
		front_id int(11) NOT NULL COMMENT '前置编号',
		chain_id int(11) NOT NULL COMMENT '链编号',
		group_id int(11) NOT NULL COMMENT '群组编号',
		block_height bigint NOT NULL COMMENT '块高',
		pbft_view bigint NOT NULL COMMENT 'view',
		pending_transaction_count int(11) NOT NULL COMMENT '待交易数',
		timestamp bigint NOT NULL COMMENT '统计时间',
		record_month int(11) NOT NULL COMMENT '统计时间，年份加月份，如202008',
		PRIMARY KEY (id,record_month),
		INDEX idx_group (group_id),
		INDEX index_front_chain (front_id,chain_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='前置节点监控数据表'
		PARTITION BY RANGE (record_month) (
		PARTITION p_default VALUES LESS THAN MAXVALUE
);

CREATE TABLE IF NOT EXISTS tb_server_performance (
		id bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
		front_id int(11) NOT NULL COMMENT '前置编号',
		chain_id int(11) NOT NULL COMMENT '链编号',
		cpu_use_ratio decimal NOT NULL COMMENT 'cpu利用率',
		disk_use_ratio decimal NOT NULL COMMENT '硬盘利用率',
		memory_use_ratio decimal NOT NULL COMMENT '内存利用率',
		rxbps decimal NOT NULL COMMENT '上行bandwith',
		txbps decimal NOT NULL COMMENT '下行bandwith',
		timestamp bigint NOT NULL COMMENT '统计时间',
		record_month int(11) NOT NULL COMMENT '统计时间，年份加月份，如202008',
		PRIMARY KEY (id,record_month),
		INDEX index_front_chain (front_id,chain_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='前置服务器性能数据表'
		 PARTITION BY RANGE (record_month) (
		 PARTITION p_default VALUES LESS THAN MAXVALUE
);