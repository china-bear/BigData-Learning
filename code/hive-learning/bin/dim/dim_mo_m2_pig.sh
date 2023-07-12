#!/bin/bash

basedir=$(cd "$(dirname "$0")" || exit; pwd)

source ${basedir}/../common_const.sh
source ${basedir}/../common_params.sh
source ${basedir}/../common_function.sh

mus="/xxx/session/cookieevents/date=${day}/type=s"
check_success ${mus}/_SUCCESS

muc="/xxx/session/cookieevents/date=${day}/type=c"
check_success ${muc}/_SUCCESS


table=dws_mv_mo_m2_stats

table_dir="/xxx/hive/warehouse/hdp_ads_dw.db/dws/dws_mv_mo_m2_stats/dt=${day}"

${HADOOP_CMD} fs -rm -r ${table_dir}

export PIG_HEAPSIZE=4096

/usr/bin/pig << EOF
REGISTER 'xxxdata/piglib/mvad/*.jar';
REGISTER 'xxxdata/piglib/thrift/*.jar';
REGISTER 'xxxdata/piglib/common/*.jar';
REGISTER 'xxxdata/piglib/elephantbird/*.jar';

SET mapred.job.queue.name 'root.bi';
SET mapreduce.output.fileoutputformat.compress false;
set mapred.min.split.size 256000000;
set mapred.max.split.size 256000000;
set mapred.min.split.size.per.node 256000000;
set mapred.min.split.size.per.rack 256000000;
set hive.exec.reducers.bytes.per.reducer 256000000;
set mapreduce.map.memory.mb 3072;
set mapreduce.map.java.opts -Xmx2600m;
set mapreduce.reduce.memory.mb 3072;
set mapreduce.reduce.java.opts -Xmx2600m;

DEFINE PARQUET_LOADER org.apache.parquet.pig.ParquetLoader();
DEFINE GET_DOMAIN xxx.dsp.log.pig.GetDomain();

SET job.name [mba-warehouse][${table}][${day}];

sdata = LOAD '${mus}' USING PARQUET_LOADER;

SA = FOREACH sdata GENERATE adspaceId,advertiserId,sourceChannel,m2id,bidMode,CASE WHEN bidMode == 'CPC' THEN 0 ELSE fee END AS fee,valid,fraud,mobileData.imeiMD5,mobileData.androidId,mobileData.oaid;

SB = FILTER SA BY valid == 1 AND fraud == 0;

SC = GROUP SB BY (adspaceId,advertiserId,sourceChannel,m2id,bidMode,imeiMD5,androidId,oaid);

SD = FOREACH SC GENERATE FLATTEN(group),COUNT(SB) AS views,0 AS clicks,SUM(SB.fee) AS costs;

SE = FOREACH SD GENERATE adspaceId,advertiserId,sourceChannel,m2id,bidMode,views,clicks,costs,imeiMD5,androidId,oaid;

cdata = LOAD '${muc}' USING PARQUET_LOADER;

CA = FOREACH cdata GENERATE adspaceId,advertiserId,sourceChannel,m2id,bidMode,CASE WHEN bidMode == 'CPM' THEN 0 ELSE fee END AS fee,valid,fraud,mobileData.imeiMD5,mobileData.androidId,mobileData.oaid;

CB = FILTER CA BY valid == 1 AND fraud == 0;

CC = GROUP CB BY (adspaceId,advertiserId,sourceChannel,m2id,bidMode,imeiMD5,androidId,oaid);

CD = FOREACH CC GENERATE FLATTEN(group),0 AS views,COUNT(CB) AS clicks,SUM(CB.fee) AS costs;

CE = FOREACH CD GENERATE adspaceId,advertiserId,sourceChannel,m2id,bidMode,views,clicks,costs,imeiMD5,androidId,oaid;

U1 = UNION SE,CE;

STORE U1 INTO '${table_dir}' USING PigStorage('\\\u001');
EOF

if [ $? != 0 ]; then
    echo "--------------------------------------------------------------------------------"
    echo "-- 数据导出失败【(dt='${day}')】"
    echo "--------------------------------------------------------------------------------"
    exit 1
else
    echo "--------------------------------------------------------------------------------"
    echo "-- 数据导出成功【(dt='${day}')】"
    echo "--------------------------------------------------------------------------------"

fi

${HIVE_CMD} << EOF
ALTER TABLE hdp_ads_dw.${table} DROP IF EXISTS PARTITION(dt='${day}');
LOAD DATA INPATH '${table_dir}' OVERWRITE INTO TABLE hdp_ads_dw.${table} PARTITION (dt = '${day}');
EOF

if [ $? != 0 ]; then
    echo "--------------------------------------------------------------------------------"
    echo "-- 加载分区数据失败【(${table}_${day}')】"
    echo "--------------------------------------------------------------------------------"
    exit 1
else
    echo "--------------------------------------------------------------------------------"
    echo "-- 成功加载分区数据【(dt='${table}_${day}')】"
    echo "--------------------------------------------------------------------------------"
fi