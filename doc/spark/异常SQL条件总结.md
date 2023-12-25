
# 万字长文详解HiveSQL执行计划   https://cloud.tencent.com/developer/article/1843298
# https://www.jianshu.com/p/40bddd5126f9  Float Double Decimal 区别
#总结工作中SQL 难以排查 导致数据错误的条件结果
## 1 数字 与 空 字符串比较
SELECT 
CASE
  WHEN ('1'  <> '' ) THEN 1
  ELSE 0
END AS is_valid

UNION ALL
SELECT 
CASE
  WHEN (1  <> '' ) THEN 1
  ELSE 0
END AS is_valid


SELECT '1'  <> ''  AS is_valid

UNION ALL
SELECT 1  <> ''  AS is_valid

## 2. 字符串 转 数字 默认INT 导致小数会截断 
spark-sql (default)> explain SELECT extension.mid  AS md5_mid
                   > ,extension.ad_real_price  AS ad_cost
                   > FROM hdp_ads_dw.dwt_search_amazing
                   > WHERE  dt = '2023-12-20'
                   > AND amazing IN ('pc_click')
                   > AND extension.is_valid  = 1
                   > AND ad_user_info.client_category_type <> 'test'
                   > AND extension.mid != ''
                   > AND extension.mid != '-'
                   > AND extension.mid IS NOT NULL
                   > AND extension.ad_real_price > 0;
plan
== Physical Plan ==
*(1) Project [extension#1507[mid] AS md5_mid#1445, extension#1507[ad_real_price] AS ad_cost#1446]
+- *(1) Filter (((((((isnotnull(extension#1507) AND isnotnull(ad_user_info#1473)) AND (cast(extension#1507[is_valid] as int) = 1)) AND NOT (ad_user_info#1473.client_category_type = test)) AND NOT (extension#1507[mid] = )) AND NOT (extension#1507[mid] = -)) AND isnotnull(extension#1507[mid])) AND (cast(extension#1507[ad_real_price] as int) > 0))
   +- FileScan orc hdp_ads_dw.dwt_search_amazing[ad_user_info#1473,extension#1507,dt#1510,amazing#1511] Batched: false, DataFilters: [isnotnull(extension#1507), isnotnull(ad_user_info#1473), (cast(extension#1507[is_valid] as int) ..., Format: ORC, Location: InMemoryFileIndex[viewfs://ss-hadoop/project/ads/hdp-ads-dw/hive/warehouse/hdp_ads_dw.db/dwt/dwt_..., PartitionFilters: [isnotnull(dt#1510), isnotnull(amazing#1511), (dt#1510 = 2023-12-20), (amazing#1511 = pc_click)], PushedFilters: [IsNotNull(extension), IsNotNull(ad_user_info), Not(EqualTo(ad_user_info.client_category_type,tes..., ReadSchema: struct<ad_user_info:struct<client_category_type:string>,extension:map<string,string>>
   
   
   spark-sql (default)> explain SELECT extension.mid  AS md5_mid
                      > ,extension.ad_real_price  AS ad_cost
                      > FROM hdp_ads_dw.dwt_search_amazing
                      > WHERE  dt = '2023-12-20'
                      > AND amazing IN ('pc_click')
                      > AND extension.is_valid  = 1
                      > AND ad_user_info.client_category_type <> 'test'
                      > AND extension.mid != ''
                      > AND extension.mid != '-'
                      > AND extension.mid IS NOT NULL
                      > AND CAST(extension.ad_real_price AS DOUBLE) > 0;
   plan
   == Physical Plan ==
   *(1) Project [extension#1642[mid] AS md5_mid#1580, extension#1642[ad_real_price] AS ad_cost#1581]
   +- *(1) Filter (((((((isnotnull(extension#1642) AND isnotnull(ad_user_info#1608)) AND (cast(extension#1642[is_valid] as int) = 1)) AND NOT (ad_user_info#1608.client_category_type = test)) AND NOT (extension#1642[mid] = )) AND NOT (extension#1642[mid] = -)) AND isnotnull(extension#1642[mid])) AND (cast(extension#1642[ad_real_price] as double) > 0.0))
      +- FileScan orc hdp_ads_dw.dwt_search_amazing[ad_user_info#1608,extension#1642,dt#1645,amazing#1646] Batched: false, DataFilters: [isnotnull(extension#1642), isnotnull(ad_user_info#1608), (cast(extension#1642[is_valid] as int) ..., Format: ORC, Location: InMemoryFileIndex[viewfs://ss-hadoop/project/ads/hdp-ads-dw/hive/warehouse/hdp_ads_dw.db/dwt/dwt_..., PartitionFilters: [isnotnull(dt#1645), isnotnull(amazing#1646), (dt#1645 = 2023-12-20), (amazing#1646 = pc_click)], PushedFilters: [IsNotNull(extension), IsNotNull(ad_user_info), Not(EqualTo(ad_user_info.client_category_type,tes..., ReadSchema: struct<ad_user_info:struct<client_category_type:string>,extension:map<string,string>>
      
      
spark-sql (default)> SELECT 0.35 > 0 as c1, 1.63> 0 as c2,  CAST('0.35' AS DOUBLE) > 0 as c3,   CAST('1.63' AS DOUBLE) > 0 as c4, CAST('0.35' AS INT) > 0 AS C5 ,  CAST('1.63' AS INT)> 0 AS C6;
23/12/22 18:18:03 INFO SparkContext: Starting job: main at NativeMethodAccessorImpl.java:0
23/12/22 18:18:03 INFO SparkContext: Created broadcast 30 from broadcast at DAGScheduler.scala:1388
c1      c2      c3      c4      C5      C6
true    true    true    true    false   true