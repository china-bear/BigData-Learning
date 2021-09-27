
一、关系运算：

等值比较: =
等值比较:<=>
不等值比较: <>和!=
小于比较: <
小于等于比较: <=
大于比较: >
大于等于比较: >=
区间比较
空值判断: IS NULL
非空判断: IS NOT NULL
LIKE比较: LIKE
（1）字符”_”表示任意单个字符，而字符”%”表示任意数量的字符
（2）注意：否定比较时候用 NOT A LIKE B hive> select 1 from table where NOT 'football' like 'fff%';
JAVA的LIKE操作: RLIKE：
如果字符串 A 符合 JAVA 正则表达式 B 的正则语法，则为 TRUE；否则为 FALSE。
REGEXP操作: REGEXP：功能与 RLIKE 相同

**二、数学运算： **

加法操作: +
减法操作: –
乘法操作: *
除法操作: /
取余操作: %
位与操作: &
位或操作: |
位异或操作: ^
9．位取反操作: ~ 注意将~运算后的值，进行补码运算
**三、逻辑运算： **

逻辑与操作: AND 、&&
逻辑或操作: OR 、||
逻辑非操作: NOT、!
**四、复合类型构造函数 **

map结构
struct结构
named_struct结构
array结构
create_union

五、复合类型操作符

获取array中的元素：
返回数组 A 中第 n 个索引的元素值：
hive> select array('a','b','c')[1] from table;
结果：b
获取map中的元素:
返回 map 结构 M 中 key 对应的 value：
hive> select map('k1','v1')['k1'] from table;
结果：v1
获取struct中的元素
返回 struct 结构 S 中名为 x 的元素：
hive> select named_struct('a',1,'b','aaa','c',FALSE).c from table;
结果：false
六、数值计算函数

取整函数: round
语法: round(double a)
返回值：BIGINT 四舍五入
指定精度取整函数: round
语法: round(double a, int d)
返回值:返回指定精度 d 的 double 类型，小数点后d位
向下取整函数: floor
向上取整函数: ceil
向上取整函数: ceiling
取随机数函数: rand
自然指数函数（返回自然对数e的a次方）: exp(double a)
自然对数函数（返回a的自然对数）：ln(double a)
以10为底对数函数: log10(double a)
以2为底对数函数: log2(double a)
对数函数: log(double base, double a) 返回以 base 为底的 a 的对数，double类型
幂运算函数: pow(double a, double p)
幂运算函数: power(double a, double p)
开平方函数: sqrt(double a) 返回a的平方根
二进制函数: bin bin(BIGINT a) 返回 a 的二进制代码表示，string类型
十六进制函数: hex
反转十六进制函数: unhex
进制转换函数: conv conv(BIGINT num, int from_base, int to_base) 将数值 num 从 from_base 进制转化到 to_base 进制
绝对值函数: abs abs(double a) / abs(int a)
正取余函数: pmod pmod(int a, int b),pmod(double a, double b) 返回正的 a 除以 b 的余数
正弦函数: sin sin(double a)
反正弦函数: asin
余弦函数: cos
反余弦函数: acos
positive函数: positive positive(int a), positive(double a) 返回a
negative函数: negative 返回-a
七、集合操作函数

map类型大小：size size(map('k1','v1','k2','v2'))

array类型大小：size size(array(1,2,3,4,5))

判断元素数组是否包含元素：array_contains
hive> select array_contains(array(1,2,3,4,5),3) from table;
true

获取map中所有value集合
hive> select map_values(map('k1','v1','k2','v2')) from table;
["k2","k1"]

获取map中所有key集合
map_keys(map('k1','v1','k2','v2'))

数组排序
hive> select sort_array(array(5,7,3,6,9)) from table;
[3,5,6,7,9]

八、类型转换函数

二进制转换：binary
基础类型之间强制转换：cast
举例：cast(name as int)： 将name字段的数据类型转为int类型
九、日期函数

UNIX时间戳转日期函数: from_unixtime
语法: from_unixtime(bigint unixtime[, string format])
返回值: string
说明: 转化 UNIX 时间戳（从 1970-01-01 00:00:00 UTC 到指定时间的秒数）到当前时区的时间格式
举例：
hive> select from_unixtime(1323308943,'yyyyMMdd') from table;
结果：20111208

获取当前UNIX时间戳函数: unix_timestamp() 无参数

日期转UNIX时间戳函数: unix_timestamp
语法: unix_timestamp(string date)
返回值: bigint
说明: 转换格式为"yyyy-MM-dd HH:mm:ss"的日期到 UNIX 时间戳。如果转化失败，则返回 0。
举例：
hive> select unix_timestamp('2011-12-07 13:01:03') from table;
结果：1323234063

指定格式日期转UNIX时间戳函数: unix_timestamp

日期时间转日期函数: to_date
语法: to_date(string timestamp)
返回值: string
说明: 返回日期时间字段中的日期部分。
举例：
hive> select to_date('2011-12-08 10:03:01') from table;
结果：2011-12-08

日期转年函数: year

日期转月函数: month

日期转天函数: day

日期转小时函数: hour

日期转分钟函数: minute

日期转秒函数: second

日期转周函数: weekofyear

日期比较函数: datediff

日期增加函数: date_add
语法: date_add(string startdate, int days)
返回值: string
说明: 返回开始日期 startdate 增加 days 天后的日期。
举例：
hive> select date_add('2012-12-08',10) from table;
结果：2012-12-18

日期减少函数: date_sub

十、条件函数

If函数: if
语法: if(boolean testCondition, T valueTrue, T valueFalseOrNull)
说明: 当条件 testCondition 为 TRUE 时，返回 valueTrue；否则返回 valueFalseOrNull
非空查找函数: COALESCE
条件判断函数：CASE
语法: CASE WHEN a THEN b [WHEN c THEN d]* [ELSE e] END
说明：如果 a 为 TRUE,则返回 b；如果 c 为 TRUE，则返回 d；否则返回 e
十一、字符串函数

字符ascii码函数：ascii

base64字符串

字符串连接函数：concat concat(string A, string B…)

带分隔符字符串连接函数：concat_ws concat_ws(string SEP, string A, string B…)，其中SEP为分隔符

数组转换成字符串的函数：concat_ws 举例：concat_ws('|',array('a','b','c'))

小数位格式化成字符串函数：format_number
语法: format_number(number x, int d)
返回值: string
说明：将数值 x 的小数位格式化成 d 位，四舍五入
举例：
hive> select format_number(5.23456,3) from table;
结果：5.235

字符串截取函数：substr,substring

字符串截取函数：substr,substring
语法：substr(string A, int start, int len),substring(string A, int start, int len)
说明：返回字符串 A 从 start 位置开始，长度为 len 的字符串

字符串查找函数： instr(string str, string substr)

字符串长度函数：length

字符串查找函数：locate
语法: locate(string substr, string str[, int pos])
说明：返回字符串 substr 在 str 中从 pos 后查找，首次出现的位置

字符串格式化函数：printf

字符串转换成map函数：str_to_map

base64解码函数：unbase64(string str)

字符串转大写函数：upper/ucase

字符串转小写函数：lower/lcase

去空格函数（去除字符串两边的空格）：trim

左边去空格函数：ltrim

右边去空格函数：rtrim

正则表达式替换函数：regexp_replace
语法: regexp_replace(string A, string B, string C)
返回值: string
说明：将字符串 A 中的符合 java 正则表达式 B 的部分替换为 C。注意，在有些情况下要
使用转义字符,类似 oracle 中的 regexp_replace 函数。
举例：
hive> select regexp_replace('foobar', 'oo|ar', '') from table;
结果：fb

正则表达式解析函数：regexp_extract

URL解析函数：parse_url
语法: parse_url(string urlString, string partToExtract [, string keyToExtract])
返回值: string
说明：返回 URL 中指定的部分。partToExtract 的有效值为：HOST, PATH, QUERY, REF,
PROTOCOL, AUTHORITY, FILE, and USERINFO.
举例：
hive> select parse_url('http://facebook.com/path1/p.php?k1=v1&k2=v2#Ref1', 'HOST') from
table;
facebook.com

json解析函数：get_json_object

空格字符串函数：space

重复字符串函数：repeat repeat(string str, int n)

左补足函数：lpad
语法: lpad(string str, int len, string pad)
返回值: string
说明：将 str 进行用 pad 进行左补足到 len 位

右补足函数：rpad

分割字符串函数: split
语法: split(string str, string pat)
返回值: array
说明: 按照 pat 字符串分割 str，会返回分割后的字符串数组
举例：
hive> select split('abtcdtef','t') from table;
["ab","cd","ef"]

集合查找函数: find_in_set

分词函数：sentences

分词后统计一起出现频次最高的TOP-K

分词后统计与指定单词一起出现频次最高的TOP-K

十二、混合函数

调用Java函数：java_method
调用Java函数：reflect
字符串的hash值：hash
十三、XPath解析XML函数

xpath
xpath_string
xpath_boolean
xpath_short, xpath_int, xpath_long
xpath_float, xpath_double, xpath_number
十四、汇总统计函数（UDAF）

个数统计函数: count
总和统计函数: sum
平均值统计函数: avg
最小值统计函数: min
最大值统计函数: max
非空集合总体变量函数: var_pop
非空集合样本变量函数: var_samp
总体标准偏离函数: stddev_pop
样本标准偏离函数: stddev_samp
10．中位数函数: percentile
中位数函数: percentile
近似中位数函数: percentile_approx
近似中位数函数: percentile_approx
直方图: histogram_numeric
集合去重数：collect_set
集合不去重函数：collect_list
十五、表格生成函数Table-Generating Functions (UDTF)

数组拆分成多行：explode
Map拆分成多行：explode

# [Hive函数介绍] https://www.jianshu.com/p/7b29853da7ef
