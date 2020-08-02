package com.chinabear.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * @program: BigData-Learning
 * @description: my lower
 * @author: Mr.Bear
 * @create: 2020-08-02 23:13
 **/

public  class MyLower extends UDF {
    public Text evaluate(final Text s) {
        if (s == null) { return null; }
        return new Text(s.toString().toLowerCase());
    }

    public static void main(String[] args){
        System.out.println(new MyLower().evaluate(new Text("HIVE")));
    }

}
