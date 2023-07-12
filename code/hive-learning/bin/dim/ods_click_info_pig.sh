#!/bin/bash

basedir=$(cd "$(dirname "$0")" || exit; pwd)
source ${basedir}/../common_const.sh
source ${basedir}/../common_params.sh
source ${basedir}/../common_function.sh

ok_flag=/ads/rawlog/dsp/${day}/${hour}
check_success "${ok_flag}/_SUCCESS"

tb_name=ods_mv_click_info


OUT_DIR="/project/ads/xxx/ods/${tb_name}/dt=${day}/hour=${hour}"

${HADOOP_CMD} fs -rm -r ${OUT_DIR}

export PIG_HEAPSIZE=4096

/usr/bin/pig << EOF
REGISTER 'ivy://xx:mv-log-format:%5b3.0,4.0%29?transitive=false';
REGISTER 'ivy://xx:mediav-daily-log:%5b3.0,4.0%29?transitive=false';
REGISTER 'ivy://xx:mv-common:%5b3.0,4.0%29?transitive=false';
REGISTER 'ivy://xx:mv-pig-farm:%5b3.0,4.0%29?transitive=false';
REGISTER 'hdfs://xx/data/piglib/thrift/*.jar';
REGISTER 'hdfs://xx/data/piglib/common/*.jar';
REGISTER 'hdfs://xx/data/piglib/elephantbird/*.jar';

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

SET job.name [pig][${tb_name}][${day}];

DEFINE Byte2Thrift xxx.thrift.pig.BytesToThrift('ambition.thrift.ImpressionExtendedInfo');
DEFINE GET_DOMAIN xxx.dsp.log.pig.GetDomain();
DEFINE LongLogId InvokeForLong('xxx.data.log.LogUtils.getLongLogId', 'String');
DEFINE IpToStr xxx.pig.IpIntToStr();
DEFINE GetTopTuple xxx.pig.GetTopTuple();

DEFINE parse_url HiveUDF('parse_url');

DATA = LOAD '/ads/rawlog/dsp/${day}/${hour}/d.{c,c-cube}.[6-7].m/pc' USING xxx.elephantbird.pig.util.NullableThriftPigLoader('xxx.data.log.unitedlog.UnitedEvent');

A = FOREACH DATA GENERATE logId
,linkedIds.mvid
,eventTime
,contextInfo.geoInfo.province
,contextInfo.geoInfo.city
,IpToStr(contextInfo.ipv4)
,advertisementInfo.exchangeId
,advertisementInfo.exchangeBidId
,advertisementInfo.impressionInfo.showInfo.impressionId
,advertisementInfo.impressionInfo.showInfo.bidRequestLogId
,advertisementInfo.impressionInfo.showInfo.creativeType
,advertisementInfo.impressionInfo.showInfo.advertiserId
,advertisementInfo.impressionInfo.showInfo.campaignId
,advertisementInfo.impressionInfo.showInfo.solutionId
,advertisementInfo.impressionInfo.showInfo.creativeId
,advertisementInfo.impressionInfo.showInfo.keywordId
,advertisementInfo.impressionInfo.showInfo.clickId
,advertisementInfo.impressionInfo.showInfo.solutionOwner
,advertisementInfo.impressionInfo.showInfo.solutionType
,advertisementInfo.impressionInfo.showInfo.targetUri
,advertisementInfo.impressionInfo.showInfo.errorFlag
,userBehavior.clickEventId
,userBehavior.sourceId
,userBehavior.adSlotClickInfo.xPosition
,userBehavior.adSlotClickInfo.yPosition
,advertisementInfo.impressionInfo.showInfo.fee
,advertisementInfo.impressionInfo.showInfo.bidMode
,parse_url(userBehavior.urlInfo.pageReferralUrl,'QUERY','ls') as ls
,IpToStr(contextInfo.ipv4)
,contextInfo.params#'bidid'
;

STORE A INTO '${OUT_DIR}' USING PigStorage('\t');
EOF

if [ $? != 0 ]; then
    echo "--------------------------------------------------------------------------------"
    echo "-- PIG数据导出失败【(dt='${day}')】"
    echo "--------------------------------------------------------------------------------"
    exit 1
else
    echo "--------------------------------------------------------------------------------"
    echo "--PIG数据导出成功【(dt='${day}')】"
    echo "--------------------------------------------------------------------------------"
fi

${HIVE_CMD} << EOF
ALTER TABLE  hdp_ads_dw.${tb_name} DROP IF EXISTS PARTITION (dt='${day}', hour='${hour}');
ALTER TABLE  hdp_ads_dw.${tb_name} ADD IF NOT EXISTS PARTITION (dt='${day}', hour='${hour}');
EOF

if [ $? != 0 ]; then
    echo "--------------------------------------------------------------------------------"
    echo "-- 分区创建失败【(${tb_name}_${day}_${hour})】"
    echo "--------------------------------------------------------------------------------"
    exit 1
else
    echo "--------------------------------------------------------------------------------"
    echo "-- 成功创建分区【(${tb_name}_${day}_${hour}')】"
    echo "--------------------------------------------------------------------------------"
fi

