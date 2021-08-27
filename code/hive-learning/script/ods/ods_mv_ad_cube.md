#  广告CUBE ODS 信息表 (mediav_base.cube)

## 建表

```sql
CREATE EXTERNAL TABLE `hdp_ads_dw.ods_mv_ad_cube` (
  `id` BIGINT   ,
  `cube_id` STRING   COMMENT 'cube id',
  `idn` STRING   COMMENT 'cube包大小，字节数',
  `name` STRING   COMMENT 'cube名称',
  `cate_id` BIGINT   COMMENT '分类id',
  `type` BIGINT   COMMENT 'cube 类型：1、广告Cube 2、小程序 3、小游戏Cube 4、商业内容Cube 5、客户Cube 6、外部Cube 7、虚拟cube',
  `tags` STRING   COMMENT '多个标签以英文逗号分隔\n',
  `contacts` STRING   COMMENT '紧急联系人[{"姓名":"xxx","邮箱":"xxx@360.cn","电话":"xxxxxx"},{"姓名":"xxx","邮箱":"xxx@360.cn","电话":"xxxxxx"}]',
  `summary` STRING   COMMENT '描述信息，选填',
  `api_white_list` STRING   COMMENT '接口数据使用的domain白名单，多个请以逗号分割',
  `ext` STRING   COMMENT '拓展字断，json格式',
  `status` BIGINT   COMMENT '上线状态，0 未上线，1 上线',
  `adspaceid` BIGINT   COMMENT '广告位id',
  `delete_flag` BIGINT   COMMENT '是否删除,1 是，0 否',
  `create_time` STRING   COMMENT '创建日期',
  `update_time` BIGINT   COMMENT '最后修改时间',
  `only_miniapp` BIGINT   COMMENT '表示只允许在小程序流量上召回，默认勾选, 1是，0否',
  `dimids` STRING   COMMENT '表示只有在人群标签包含这类人群时，才召回,多个值用逗号分隔，默认为空',
  `adspaces_list` STRING   COMMENT '绑定的广告位id，多个用英文逗号分隔',
  `adspaceid_source` BIGINT   COMMENT '导流广告位id',
  `version_code` BIGINT   COMMENT '拉取cube 获取的版本号',
  `account_id` STRING   COMMENT 'cube开放平台对应账号id',
  `cate_name` STRING   COMMENT 'cube开放平台对应的类别名称',
  `cube_tags` STRING   COMMENT 'cube开放平台对应的标签，多个以英文逗号分隔'
)  COMMENT 'cube信息表'
PARTITIONED BY (`dt` STRING COMMENT '时间分区' )
ROW FORMAT DELIMITED
  FIELDS TERMINATED BY '\t'
  COLLECTION ITEMS TERMINATED BY '\002'
  MAP KEYS TERMINATED BY '\003'
STORED AS TEXTFILE
LOCATION '/xxx/hive/warehouse/hdp_ads_dw.db/ods/ods_mv_ad_cube';
```
