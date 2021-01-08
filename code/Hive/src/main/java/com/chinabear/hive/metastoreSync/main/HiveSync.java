package com.chinabear.hive.metastoreSync.main;

import com.chinabear.hive.metastoreSync.common.Tools;
import com.chinabear.hive.metastoreSync.hive.Commands;
import com.chinabear.hive.metastoreSync.hive.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.HashSet;
import java.util.List;


public class HiveSync {

  private Connection con2;
  private Connection con1;
  private List<String> dbWildcards;

  private static final Logger LOGGER = LoggerFactory.getLogger(HiveSync.class);

  public HiveSync(String src, String srcUser, String srcPass, String dst, String dstUser,
      String dstPass, List<String> databases) throws Exception {
    con1 = Tools.createNewConnection(src, srcUser, srcPass);
    con2 = Tools.createNewConnection(dst, dstUser, dstPass);
    this.dbWildcards = databases;
  }

  public void execute() throws Exception {
    HashSet<String> dbList1 = new HashSet<>();
    for (String database : dbWildcards) {
      dbList1.addAll(Commands.getDatabases(con1, database));
    }
    HashSet<String> dbList2 = new HashSet<>();
    for (String database : dbWildcards) {
      dbList2.addAll(Commands.getDatabases(con2, database));
    }

    LOGGER.info("Detect file system information");
    String fs1 = Commands.getFsDefaultName(con1);
    String fs2 = Commands.getFsDefaultName(con2);
    LOGGER.info("Source file system: " + fs1);
    LOGGER.info("Destination file system: " + fs2);

    for (String db : dbList1) {
      LOGGER.info("Syncing database: " + db);
      if (!dbList2.contains(db)) {
        createDatabase(con2, db);
      }
      syncDatabase(db, fs1, fs2);
    }
  }

  private void createDatabase(Connection con, String db) throws Exception {
    LOGGER.info("Create database: " + db);
    Commands.createDatabase(con, db);
  }

  private void syncDatabase(String database, String fs1, String fs2) throws Exception {
    LOGGER.info("Collect table information");

    List<TableInfo> srcTables = Commands.getTables(con1, database);
    List<TableInfo> dstTables = Commands.getTables(con2, database);

    for (TableInfo srcTable : srcTables) {
      TableInfo dstTable = findTable(dstTables, srcTable.getName());
      if (dstTable != null) {
        LOGGER.info("Re-create existing table: " + dstTable.getName());
        Commands.recreateTable(con2, srcTable, dstTable, fs1, fs2);
      } else {
        LOGGER.info("Create non-existing table: " + srcTable.getName());
        Commands.createTable(con2, srcTable, fs1, fs2);
      }
    }
    for (TableInfo dstTable : dstTables) {
      if (findTable(srcTables, dstTable.getName()) == null) {
        LOGGER.info("Drop table: " + dstTable.getName());
        Commands.dropTable(con2, dstTable);
      }
    }
  }

  private TableInfo findTable(List<TableInfo> tables, String tableName) {
    for (TableInfo ti : tables) {
      if (ti.getName().equals(tableName)) {
        return ti;
      }
    }
    return null;
  }

}
