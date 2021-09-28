package examples.windows;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.dense_rank;
import static org.apache.spark.sql.functions.max;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;

public class TopNPerGroup {

    public static void main(String[] args) {

        Logger.getLogger("org").setLevel(Level.ERROR);
        Logger.getLogger("akka").setLevel(Level.ERROR);

        // Create a session
        SparkSession spark = new SparkSession.Builder()
                .appName("Top N Values by Category")
                .master("local")
                .getOrCreate();

        // Get Data
        Dataset<Row> product_revenue = spark.read().format("csv")
                .option("header", true)
                .load("src/main/resources/product_revenue.csv");

        System.out.println("## Product Dataset ");

        product_revenue.show();

        WindowSpec byCatgRevnuDesc = Window.partitionBy("category").orderBy(col("revenue").desc());

        Column rankbyDeptName = dense_rank().over(byCatgRevnuDesc);

        System.out.println("## Top 2 Products per category, ranked by higher revenue ");

        product_revenue.withColumn("rank", rankbyDeptName).where(col("rank").leq(2)).show();

        Column revenueDiff = max("revenue").over(byCatgRevnuDesc).minus(col("revenue"));

        System.out.println("## Revenue Difference per Product in each Category");

        product_revenue.withColumn("reveDiff", revenueDiff).show();

    }

}
