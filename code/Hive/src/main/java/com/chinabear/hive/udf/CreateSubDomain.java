package com.chinabear.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hanry on 2019/03/04.
 * <p>
 * create temporary function company_num_statistic_dim as 'com.qihoo.sydata.dw.udf.ImCompanyNumStatistic';
 */
public class CreateSubDomain extends GenericUDTF {

    @Override
    public StructObjectInspector initialize(StructObjectInspector argOIs) throws UDFArgumentException {
        ArrayList<String> fieldNames = new ArrayList<String>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();

        fieldNames.add("subdomain");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        //定义了行的列数和类型
        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames,fieldOIs);

    }

    @Override
    public void process(Object[] args) throws HiveException {
        if (args.length != 1) {
            throw new HiveException("NameParserGenericUDTF() takes exactly one argument");
        }

        List<String> resultList = generateData(args[0].toString());
       // forward(resultList.toArray(new String[resultList.size()]));
        for(String str : resultList) {
            forward(str.split("###"));
        }

    }

    public static List<String> generateData(String host){
        //subDomain
        String[] strings =host.split("\\.");

        List<String> resultList = new ArrayList<String>();
        if (strings == null || strings.length==0) {
            return resultList;
        }
        int str_length = strings.length;
        int jMax = str_length -1;
        for (int i = 0; i<str_length ; i++) {

            StringBuilder bb = new StringBuilder();
            for (int j = i; j<str_length ; j++) {
                bb.append(strings[j]);
                if (j == jMax) {
                    resultList.add(bb.toString());
                    break;
                }
                bb.append(".");
            }
        }
        return resultList;
    }

    @Override
    public void close() throws HiveException {

    }

}
