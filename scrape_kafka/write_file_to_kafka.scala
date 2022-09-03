import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.DataFrame
//
//val spark = SparkSession.builder().master("local").appName("File Sink")
//  .getOrCreate()
//


def process_data(df: DataFrame): DataFrame ={
  val base_time = "2015-08-01T00:00:21"
  return df.withColumn("value", from_json(col("value"), MapType(StringType, StringType))).select(col("value").getItem("session_id").as("session_id"),
    col("value").getItem("event_type").as("event_type"),
    col("value").getItem("course_id").as("course_id"),
    col("value").getItem("event_time").as("event_time"),
    col("value").getItem("user_id").as("user_id")).
    withColumn("event_time", date_add(date_sub(current_date(), 1), datediff(col("event_time"), base_time))).
    withColumn("event_place", split(col("event_type"), "_").getItem(1)).
    withColumn("timestamp", unix_timestamp(col("event_time"), "yyyy-MM-dd'T'HH:mm:ss")).
    withColumn("event_date", date_format(col("event_time"), "yyyy-MM-dd")).
    withColumn("event_time", date_format(col("event_time"), "HH:mm:ss"))
}


def read_data(path: String): DataFrame ={
  val schema = StructType(List(
    StructField("value", StringType, false)))
  spark.readStream
    .option("maxFilesPerTrigger", 2) // This will read maximum of 2 files per mini batch. However, it can read less than 2 files.
    .option("header", true)
    .schema(schema)
    .text(path)
}



val path_format = "/home/sahel/personal/university/computerTech/project/results/result_test/*.json"
val df = process_data(read_data(path_format))
val columns = Array("session_id", "event_type", "course_id", "event_time", "user_id", "event_place", "timestamp", "event_date")
//df.writeStream.format("console").start()

df.withColumn("key", col("course_id")).withColumn("value", to_json(struct(columns.map(col(_)):_*))).select("key", "value").writeStream.format("kafka").option("kafka.bootstrap.servers", "localhost:9093,localhost:9092").option("checkpointLocation", "/tmp/sahel/checkpoint").option("topic", "myorg_topic").start().awaitTermination()
