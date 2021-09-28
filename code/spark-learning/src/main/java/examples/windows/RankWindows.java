package examples.windows;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.rank;

public class RankWindows {

    public static void main(String[] args) {

        Logger.getLogger("org").setLevel(Level.ERROR);
        Logger.getLogger("akka").setLevel(Level.ERROR);

        // Create a session
        SparkSession spark = new SparkSession.Builder()
                .appName("Windows OrderBy & Ranking")
                .master("local")
                .getOrCreate();

        // Get Data
        Dataset<Row> empDeptDF = spark.read().format("csv")
                .option("header", true)
                .load("src/main/resources/emp_dept.csv");

        System.out.println("## Employee Department Dataset ");

        empDeptDF.show();

        // Create Window partitioned by 'depName' column and order the partitioned rows by desc order of 'salary'.

        WindowSpec depNameSalDescWindow = Window.partitionBy(col("depName")).orderBy(col("salary").desc());

        // a numerical rank within the current row's partition for each distinct ORDER BY value
        // Here rank will be created within each partition i.e. for each department &
        // for each distinct ORDER By value of 'salary'

        Column rankbyDeptName = rank().over(depNameSalDescWindow);

        empDeptDF.withColumn("rank", rankbyDeptName).show();

        spark.close();

    }

}
