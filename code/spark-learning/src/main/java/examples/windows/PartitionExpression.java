package examples.windows;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.count;
import static org.apache.spark.sql.functions.sum;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;

public class PartitionExpression {

    public static void main(String[] args) {

        Logger.getLogger("org").setLevel(Level.ERROR);
        Logger.getLogger("akka").setLevel(Level.ERROR);

        // Create a session
        SparkSession spark = new SparkSession.Builder()
                .appName("Partition by expressions")
                .master("local")
                .getOrCreate();

        // Get Data
        Dataset<Row> employees = spark.read().format("csv")
                .option("header", true)
                .load("src/main/resources/employee.csv");

        System.out.println("## Employee Dataset ");

        employees.show();

        WindowSpec startNempNameWindow = Window.partitionBy(col("empName").startsWith("N"));

        Dataset<Row> startNemployeeCount =  employees.withColumn("sum of 'N' employees", count("*")
                .over(startNempNameWindow));

        System.out.println("## Partitioned Employee Dataset into 2 sets, one start with 'N' and remaining");

        startNemployeeCount.show();

        spark.close();

    }

}
