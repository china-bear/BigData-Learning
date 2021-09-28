package examples.windows;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;

import static org.apache.spark.sql.functions.sum;
import static org.apache.spark.sql.functions.col;

public class CumulativeSum {

    public static void main(String[] args) {

        Logger.getLogger("org").setLevel(Level.ERROR);
        Logger.getLogger("akka").setLevel(Level.ERROR);

        // Create Spark Session
        SparkSession spark = new SparkSession.Builder()
                .appName("Cumulative Sum using Windows functions").master("local").getOrCreate();

        // Read Customers Data
        Dataset<Row> customers = spark.read().format("csv")
                        .option("header", true)
                        .load("src/main/resources/customers.csv");

        System.out.println("## Customers Dataset ");
        customers.show();

        System.out.println("## Customers Data with Cumulative Sum of amount spent ");

        WindowSpec byNameNDate = Window.partitionBy("name").orderBy("date").rowsBetween(Long.MIN_VALUE, 0);

        customers.withColumn("cumulativeSum", sum(col("amountSpent")).over(byNameNDate)).show();
    }

}
