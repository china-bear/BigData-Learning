package com.chinabear.hive.hclient;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.IMetaStoreClient;
import org.apache.hadoop.hive.metastore.RetryingMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.Database;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.thrift.TException;

import java.util.List;

public class HiveClient {


    IMetaStoreClient client;

    public HiveClient() {
        try {
            HiveConf hiveConf = new HiveConf();
            //一种简单的配置方法，就是直接将hive-site.xml通过添加文件的方式加载到配置
            //hiveConf.addResource("hive-site.xml");
            client = RetryingMetaStoreClient.getProxy(hiveConf, true);
        } catch (MetaException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<String> getAllDatabases() {
        List<String> databases = null;
        try {
            databases = client.getAllDatabases();
        } catch (TException ex) {
            System.out.println(ex.getMessage());
        }
        return databases;
    }

    public Database getDatabase(String db) {
        Database database = null;
        try {
            database = client.getDatabase(db);
        } catch (TException ex) {
            System.out.println(ex.getMessage());
        }
        return database;
    }

    public List<FieldSchema> getSchema(String db, String table) {
        List<FieldSchema> schema = null;
        try {
            schema = client.getSchema(db, table);
        } catch (TException ex) {
            System.out.println(ex.getMessage());
        }
        return schema;
    }

    public List<String> getAllTables(String db) {
        List<String> tables = null;
        try {
            tables = client.getAllTables(db);
        } catch (TException ex) {
            System.out.println(ex.getMessage());
        }
        return tables;
    }

    public String getLocation(String db, String table) {
        String location = null;
        try {
            location = client.getTable(db, table).getSd().getLocation();
        }catch (TException ex) {
            System.out.println(ex.getMessage());
        }
        return location;
    }
}


