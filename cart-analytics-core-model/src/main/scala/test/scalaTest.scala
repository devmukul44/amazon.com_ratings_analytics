package test

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SQLContext, SaveMode}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

/**
  * Created by Mukul Dev on 3/4/17.
  */
object scalaTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("smartCart")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR);

    println("--------------------- Amazon Products ----------------------------")
    val productsInputPath = "/home/mukul/Documents/smart_cart/data/amazon/Amazon-GoogleProducts/Amazon.csv"
    val productsDF = sqlContext.read
      .format("com.databricks.spark.csv")
      .option("header","true")
      .load(productsInputPath)
    productsDF.show()

    println("-------------------- Electronics Ratings ----------------------")
    val electronicsRatingsPath = "/home/mukul/Documents/smart_cart/data/amazon/category/ratings_Electronics.csv"
    val electronicsRatingsDF = sqlContext.read
      .format("com.databricks.spark.csv")
      .option("header","false")
      .option("inferSchema", "true")
      .load(electronicsRatingsPath)
      .withColumnRenamed("C0","person_id")
      .withColumnRenamed("C1","prod_id")
      .withColumnRenamed("C2","rating")
      .withColumnRenamed("C3","timeStamp")
    electronicsRatingsDF.show()

    println("------------------ cloths shoes jewelry Ratings -----------------")
    val clothsShoesJewelryRatingsPath = "/home/mukul/Documents/smart_cart/data/amazon/category/ratings_Clothing_Shoes_and_Jewelry.csv"
    val clothsShoesJewelryRatingsDF = sqlContext.read
      .format("com.databricks.spark.csv")
      .option("header","false")
      .option("inferSchema", "true")
      .load(clothsShoesJewelryRatingsPath)
      .withColumnRenamed("C0","person_id")
      .withColumnRenamed("C1","prod_id")
      .withColumnRenamed("C2","rating")
      .withColumnRenamed("C3","timeStamp")
      .persist()
    clothsShoesJewelryRatingsDF.show()

    println("------------------ Books Ratings -----------------")
    val booksRatingsPath = "/home/mukul/Documents/smart_cart/data/amazon/category/ratings_Books.csv"
    val booksRatingsDF = sqlContext.read
      .format("com.databricks.spark.csv")
      .option("header","false")
      .option("inferSchema", "true")
      .load(booksRatingsPath)
      .withColumnRenamed("C0","person_id")
      .withColumnRenamed("C1","prod_id")
      .withColumnRenamed("C2","rating")
      .withColumnRenamed("C3","timeStamp")
      .persist()
    booksRatingsDF.show()

    println("------------------ phones and accessories Ratings -----------------")
    val mobileRatingsPath = "/home/mukul/Documents/smart_cart/data/amazon/category/ratings_Cell_Phones_and_Accessories.csv"
    val mobileRatingsDF = sqlContext.read
      .format("com.databricks.spark.csv")
      .option("header","false")
      .option("inferSchema", "true")
      .load(mobileRatingsPath)
      .withColumnRenamed("C0","person_id")
      .withColumnRenamed("C1","prod_id")
      .withColumnRenamed("C2","rating")
      .withColumnRenamed("C3","timeStamp")
      .persist()
    mobileRatingsDF.show()

    println("------------------ office products Ratings -----------------")
    val officeProductsRatingsPath = "/home/mukul/Documents/smart_cart/data/amazon/category/ratings_Office_Products.csv"
    val officeProductsRatingsDF = sqlContext.read
      .format("com.databricks.spark.csv")
      .option("header","false")
      .option("inferSchema", "true")
      .load(officeProductsRatingsPath)
      .withColumnRenamed("C0","person_id")
      .withColumnRenamed("C1","prod_id")
      .withColumnRenamed("C2","rating")
      .withColumnRenamed("C3","timeStamp")
      .persist()
    officeProductsRatingsDF.show()

    println("---------- book grouped ratings --------------")
    val bookRatingRDD = booksRatingsDF
      .select(booksRatingsDF("prod_id"), booksRatingsDF("rating")).rdd
      .groupBy (row => row.getAs[String]("prod_id"))
      .map{tuple =>
        val ratingList = tuple._2.map(row => row.getAs[Double]("rating")).toList
        val ratingSum = ratingList.sum
        val ratingListCount = ratingList.size
        val rating = ratingSum / ratingListCount
        Row(tuple._1, ratingListCount, rating)
      }
    val structType = new StructType(
      Array(
        StructField("prod_id", StringType),
        StructField("number_of_ratings", IntegerType),
        StructField("rating", DoubleType)
      ))
    val bookGroupedRatingsDF = sqlContext.createDataFrame(bookRatingRDD , structType)

//    bookGroupedRatingsDF
//      .coalesce(1)
//      .write
//      .format("com.databricks.spark.csv")
//      .option("header","true")
//      .mode(SaveMode.Overwrite)
//      .save("/home/mukul/Documents/projects/cart-analytics-core/static/book-csv")

    val getRatingClass = udf((rating: String) => {
      val trimmedCtc = rating.trim
      trimmedCtc match {
        case x if x <= "1.0" => "1 (0.1 - 1.0)"
        case x if x > "1.0" && x <=  "2.0" => "2 (1.1 - 2.0)"
        case x if x > "2.0" && x <=  "3.0" => "3 (2.1 - 3.0)"
        case x if x > "3.0" && x <=  "4.0" => "4 (3.1 - 4.0)"
        case _ => "5 (4.1 - 5.0)"
      }
    })

    import sqlContext.implicits._

    val bookRatingsClassDF = bookGroupedRatingsDF.withColumn("ratingClass", getRatingClass($"rating"))
    bookRatingsClassDF.show()

    println("------------ category book insights ---------------")
    val total = bookRatingsClassDF.count()
    val bookFinalDF = bookRatingsClassDF
      .select(bookRatingsClassDF("prod_id"), bookRatingsClassDF("ratingClass"))
      .groupBy("ratingClass")
      .agg(
        count("*").alias("no_of_Apps_by_ratingClass"),
        count("*").multiply(100).divide(total).cast("integer").alias("percentage_of_ratingClass")
      )
    bookFinalDF.show()

//    bookFinalDF
//      .coalesce(1)
//      .write
//      .format("com.databricks.spark.csv")
//      .option("header","true")
//      .mode(SaveMode.Overwrite)
//      .save("/home/mukul/Documents/projects/cart-analytics-core/static/book-rating-csv")
  }
}