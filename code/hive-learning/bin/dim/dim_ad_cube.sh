#!/bin/bash
#name: Mr.bear
#time: 2021-08-08
#info: 广告CUBE信息
#version: 1.0

basedir=$(cd `dirname $0`; pwd)

source ${basedir}/../common_params.sh
source ${basedir}/../common_const.sh
source ${basedir}/../common_function.sh

table=dim_ad_cube

${HIVE_CMD} << EOF
${hive_parematers}
set mapred.job.name=Hive:[mdw][${table}][${day}];
set hive.exec.scratchdir=/xxx/hive/scratch;

ALTER TABLE hdp_ads_dw.${table} DROP IF EXISTS PARTITION (dt='${day}');
INSERT OVERWRITE TABLE hdp_ads_dw.${table} PARTITION (dt = '${day}')
SELECT    cube_id           AS        cube_id
         ,name              AS        cube_name
         ,type              AS        cube_type
         ,status            AS        cube_status
         ,delete_flag       AS        delete_flag
         ,adspaces_list     AS        ad_space_id_list
         ,adspaceid_source  AS        from_ad_space_id
FROM hdp_ads_dw.ods_mv_ad_cube
WHERE dt = '${day}';
EOF