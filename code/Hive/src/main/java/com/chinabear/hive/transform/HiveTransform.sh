#!/usr/bin/env bash


#http://huangc.top/2018/10/08/Hive%E5%BC%80%E5%8F%913-2018/
#https://www.coder4.com/archives/4052
#UDF
HSQL="
    use cf_tmp;
    add file /xxx/to_upper.py;
    select transform(pin)
           using 'python to_upper.py'
           as upper_pin
    from cf_tmp.tmp_data_hc;
"

#UDAF
HSQL="
    use cf_tmp;
    add file /xxx/sum.py;
    select transform(a.pin, a.age)
            using 'python sum.py'
            as (pin, meaningless_age)
    from (
        select pin,
            age
        from tmp.tmp_data_hc cluster by pin
    ) a
"

#UDTF
HSQL="
    use cf_tmp;
    add file  /xxx/explode.py;
    select transform(click_list)
            using 'python explode.py'
            as single_click
    from tmp.tmp_data_hc;
"
