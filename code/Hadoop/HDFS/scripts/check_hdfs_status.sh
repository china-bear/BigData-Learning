#!/bin/sh
declare -A hdfs_status_dict
timestamp=$(date +%s)
# QUOTA  REMAINING_QUOTA  SPACE_QUOTA     REMAINING_SPACE_QUOTA   DIR_COUNT   FILE_COUNT  CONTENT_SIZE  		FILE_NAME
# 120        85 		     1841681976000    29258516078805         2158817      71178829      1785173102107    /home/hdp-ads-abc
hdfs_dir_status=$(hadoop  fs  -count  -q /home/hdp-ads-abc/)
remaining_file_quota=$(echo ${hdfs_dir_status} | awk '{if(($1=="none") && ($2=="inf")) {print "0"} else {printf "%.2f\n",($2/$1)*100}}')
space_quota=$(echo ${hdfs_dir_status} | awk '{print $3}')
content_size=$(echo ${hdfs_dir_status} |awk '{print $7}')
space_quota_usage=$(awk 'BEGIN{printf "%.2f\n",('$content_size'*3/'$space_quota')*100}')
remaining_space_quota=$(echo ${hdfs_dir_status} | awk '{print $4}')
remaining_space_quota=$(awk 'BEGIN{printf "%.2f\n",('$remaining_space_quota'/1024/1024/1024/1024)}')
file_count=$(echo ${hdfs_dir_status} | awk '{print $6}')

wonderurl="http://open.xxx.xxx.xxx/xxxx"
Token="Token:xxxx"
idc=$(hostname |awk -F . '{print $3}')
resource_name="lycc_hdp_ads_audit"

hdfs_status_dict=([hdfs_file_usage]=${remaining_file_quota} [hdfs_space_quota_usage]=${space_quota_usage} [hdfs_space_quota]=${remaining_space_quota} [hdfs_file_count]=${file_count})

# 所有key值 ${!dict[*]}  所有value ${dict[*]}
for key in $(echo ${!hdfs_status_dict[*]})
do
	echo -n "$(date '+%Y-%m-%d %H:%M:%S')   "
	httpbody='{"endpoint":"lycc_hdp_ads_abc", "metric":"'${key}'", "value":'${hdfs_status_dict[$key]}', "step":43200, "counterType":"GAUGE", "tags":"idc='${idc}'", "timestamp":'${timestamp}'}'
	curl --retry 0 --connect-timeout 3 -m 5  -H "${Token}" -d "${httpbody}" "${wonderurl}"
	echo  -e "\n"
done 
