#!/bin/bash

#数据库信息
HOSTNAME="127.0.0.1"
PORT="3306"
USERNAME="root"
PASSWORD="123456"
 #数据库名称
DBNAME="webasestat"
#数据库中表的名称
TABLE_NAME_LIST="tb_group_basic_data tb_node_monitor tb_server_performance "

############crontab command#######
# crontab -e


YEAR_VALUE=$(date +%Y)
MONTH_VALUE=$(date +%m)
# MONTH_VALUE=12

# get year*100 + month, 2020-08 => 202009, 202012 => 202013
getPartitionValue(){
	echo $(expr $(expr $1 \* 100) + $2 + 1)
}

# get year*100 + month, 2020-08 => 2020208
getPartitionName(){
	echo $(expr $(expr $1 \* 100) + $2)
}

#当前月的partition less than的值
PARTITION_VALUE=$(getPartitionValue $YEAR_VALUE $MONTH_VALUE)


#获取下一个月的partition值
getNextMonthPartition(){
	# IF MONTH IS 12, NEXT MONTH IS 1, YEAR++
	if [ $MONTH_VALUE == 12 ]; then
		YEAR_NEW=$(expr $YEAR_VALUE + 1)
		MONTH_NEW=1
		# echo $(expr $(expr $YEAR_NEW \* 100) + $MONTH_NEW)
		echo $(getPartitionValue $YEAR_NEW $MONTH_NEW)

	else
		echo $(expr $PARTITION_VALUE + 1)
	fi
}

alter(){
	printf "==========================================\n"
	printf "start alter db tables $TABLE_NAME_LIST \n"
	printf "==========================================\n"
	local PARTITION_VALUE_LOCAL=$1
	local PARTITION_NAME_LOCAL=$2
	for table in $TABLE_NAME_LIST;
	do
		# 更新p_default
		alter_partition_sql="alter table $DBNAME.$table REORGANIZE partition p_default INTO (partition p_$PARTITION_NAME_LOCAL values less than ($PARTITION_VALUE_LOCAL),PARTITION p_default values less than MAXVALUE)"
		echo "executing sql $alter_partition_sql"
		mysql -h${HOSTNAME}  -P${PORT}  -u${USERNAME} -p${PASSWORD} -e"${alter_partition_sql}"
	done
	echo "========end alter db tables========"

}

# 定时任务不执行该函数，该函数由用户初始化
# init当前月和下一个月
initNow(){
	echo "====================initNow start===================="
	echo "year is $YEAR_VALUE"
	echo "month is $MONTH_VALUE"
	# 初始化当前月
	echo "this month partition value is less than $PARTITION_VALUE"
	# local PARTITION_NAME=$(expr $PARTITION_VALUE - 1)
	echo $(alter $PARTITION_VALUE $(expr $PARTITION_VALUE - 1))
	# 初始化下一个月
	PARTITION_VALUE_NEXT=$(getNextMonthPartition)
	echo "next month partition value is $PARTITION_VALUE_NEXT"
	echo $(alter $PARTITION_VALUE_NEXT $(expr $PARTITION_VALUE_NEXT - 1))
	echo "====================initNow end===================="
}


# 定时执行，init下一个月
initNext(){
	echo "====================initNext start==================="
	echo "year is $YEAR_VALUE"
	echo "month is $MONTH_VALUE"
	# 仅初始化下一个月
	PARTITION_VALUE_NEXT=$(getNextMonthPartition)
	echo "next month partition is less than $PARTITION_VALUE_NEXT"
	echo $(alter $PARTITION_VALUE_NEXT $(expr $PARTITION_VALUE_NEXT - 1))
	echo "====================initNext end==================="
}

# stat-partition.sh一定执行该函数
# initNext

case $1 in
initNow)
    initNow;;
*)
    initNext
esac


# alter table test REORGANIZE partition p_MAX INTO (partition p_4 values less than (202011),PARTITION p_MAX values less than MAXVALUE);
# CREATE TABLE IF NOT EXISTS test (
#     id bigint NOT NULL COMMENT '编号',
#     chain_id int(11) NOT NULL COMMENT '链编号',
#     front_id int(11) NOT NULL COMMENT '前置编号',
#     status varchar(256) NOT NULL COMMENT '健康度内容',
#     description varchar(512) COMMENT '健康度内容（异常信息）',
#     record_time bigint NOT NULL COMMENT '统计时间，timestamp',
#     record_month int(11) NOT NULL COMMENT '统计时间，年份加月份，如202008',
#     create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
#     modify_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
#     PRIMARY KEY (id,record_month)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节点健康度数据表'
# PARTITION BY RANGE (record_month) (
#     PARTITION p_default VALUES LESS THAN MAXVALUE
# );