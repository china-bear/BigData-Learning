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

public class MovingWindows {

    public static void main(String[] args) {

        Logger.getLogger("org").setLevel(Level.ERROR);
        Logger.getLogger("akka").setLevel(Level.ERROR);

        // Create a session
        SparkSession spark = new SparkSession.Builder()
                .appName("Moving Avergaes N Cumulative Sum Demo")
                .master("local")
                .getOrCreate();

        // Get Data
        Dataset<Row> customers = spark.read().format("csv")
                .option("header", true)
                .load("src/main/resources/customers.csv");

        System.out.println("## Customers Dataset ");

        customers.show();

        WindowSpec byNamenDateOrderAsc = Window.partitionBy("name").orderBy("date").rowsBetween(-1, 1);

        System.out.println("## Customers Moving Avg");

        customers.withColumn("movingAvg", avg(col("amountSpent")).over(byNamenDateOrderAsc)).show();

    }

}
