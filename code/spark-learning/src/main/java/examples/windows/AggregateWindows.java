package examples.windows;

import static org.apache.spark.sql.functions.avg;
import static org.apache.spark.sql.functions.col;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;

public class AggregateWindows {

    public static void main(String[] args) {

        Logger.getLogger("org").setLevel(Level.ERROR);
        Logger.getLogger("akka").setLevel(Level.ERROR);

        // Create a session
        SparkSession spark = new SparkSession.Builder()
                .appName("Windows Aggregate Functions")
                .master("local")
                .getOrCreate();

        // Get Data
        Dataset<Row> empDeptDF = spark.read().format("csv")
                    .option("header", true)
                    .load("src/main/resources/emp_dept.csv");

        System.out.println("## Employee Department Dataset ");

        empDeptDF.show();

        // Create Window partitioned by 'depName' column.

        WindowSpec depNameWindow = Window.partitionBy("depName");

        // Apply the given Window on each row of empDeptDF and calculate Avg salary using avg aggregate function.

        empDeptDF = empDeptDF.withColumn("avg_salary", avg(col("salary")).over(depNameWindow));

        System.out.println("## Employee Department Dataset with AVG salary for each employee per department ");

        empDeptDF.show();

        spark.close();

    }

}
