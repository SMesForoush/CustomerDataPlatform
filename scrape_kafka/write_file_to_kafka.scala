import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.streaming.Trigger
//
//val spark = SparkSession.builder().master("local").appName("File Sink")
//  .getOrCreate()
//


def process_data(df: DataFrame): DataFrame ={
  return df.withColumn("value", from_json(col("value"), MapType(StringType, StringType))).select(col("value").getItem("session_id").as("session_id"),
    col("value").getItem("event_type").as("event_type"),
    col("value").getItem("course_id").as("course_id"),
    col("value").getItem("event_time").as("event_time"),
    col("value").getItem("user_id").as("user_id")).
    withColumn("event_place", split(col("event_type"), "_").getItem(1)).
    withColumn("timestamp", unix_timestamp(col("event_time"), "yyyy-MM-dd'T'HH:mm:ss")).
    withColumn("event_date", date_format(col("event_time"), "yyyy-MM-dd")).
    withColumn("event_time", date_format(col("event_time"), "HH:mm:ss"))
}


def read_data(path: String): DataFrame ={
  val schema = StructType(List(
    StructField("value", StringType, false)))
  spark.readStream
    .option("maxFilesPerTrigger", 1)
    .option("header", true)
    .schema(schema)
    .text(path)
}


def generateData(): Row = {
  val event_types = List('close_courseware',
'play_video',
'click_about',
'click_forum',
'problem_check_incorrect',
'create_thread',
'seek_video',
'problem_check_correct',
'reset_problem',
'create_comment',
'load_video',
'problem_get',
'pause_video',
'delete_comment',
'problem_check',
'click_courseware',
'stop_video',
'problem_save',
'delete_thread',
'click_progress',
'click_info',
'close_forum',
'close_info')
  val  timstamp =  LocalDateTime.now()
  val r = scala.util.Random
  val event = event_types(r.nextInt(t.size))
  val sample_row = Row(s'${r.nextInt(100)}', event, s'${r.nextInt(100)}', DateTimeFormatter.ofPattern("HH:mm:ss").format(timstamp),  s'${r.nextInt(0)}', event.split("_")(1), s'${r.nextInt(0)}', DateTimeFormatter.ofPattern("yyyy-MM-dd").format(timstamp))
  sample_row
}
val path_format = "/home/abolfazl/data/result1/20150801-20151101-raw_user_activity-converted.json"
val df = process_data(read_data(path_format))
val columns = Array("session_id", "event_type", "course_id", "event_time", "user_id", "event_place", "timestamp", "event_date")
//df.writeStream.format("console").start()

val myQueueRDD= scala.collection.mutable.Queue[RDD[Row]]()
val myStream= ssc.queueStream(myQueueRDD)

while (true) {

  val randomData = generateData() //Generated random data
  val rdd = ssc.sparkContext.parallelize(randomData) //Creates the rdd of the random data.
  myQueueRDD+= rdd //Addes data to queue.
}

myStream.foreachRDD(rdd => rdd.mapPartitions(data => evaluate(data)))