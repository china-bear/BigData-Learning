# 广告CUBE信息

## 建表

```sql
CREATE TABLE `hdp_ads_dw.dim_ad_cube`(
  cube_id           STRING  COMMENT 'cube id',
  cube_name         STRING  COMMENT 'cube名称',
  cube_type         TINYINT COMMENT 'cube类型[1=广告Cube|2=小程序|3=小游戏Cube|4=商业内容Cube|5=客户Cube|6=外部Cube|7=虚拟cube]',
  cube_status       TINYINT COMMENT '上线状态[0=未上线|1=上线]',
  delete_flag       TINYINT COMMENT '是否删除[1=是|0=否]',
  ad_space_id_list  STRING  COMMENT '绑定的广告位id用英文逗号分隔',
  from_ad_space_id  BIGINT  COMMENT '导流广告位id'
) COMMENT 'CUBE信息'
PARTITIONED BY (`dt` STRING COMMENT '时间分区' )
ROW FORMAT DELIMITED
  FIELDS TERMINATED BY '\001'
  COLLECTION ITEMS TERMINATED BY '\002'
  MAP KEYS TERMINATED BY '\003'
STORED AS ORC
LOCATION '/xxx/hive/warehouse/hdp_ads_dw.db/dim/dim_ad_cube'
TBLPROPERTIES (
'orc.compress'='ZLIB',
'orc.create.index'='true',
'orc.bloom.filter.columns'='',
'orc.bloom.filter.fpp'='0.05',
'orc.stripe.size'='268435456',
'orc.row.index.stride'='10000'
);
```
