
import com.github.nscala_time.time.Imports.DateTime
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties
import scala.util.control.Breaks._

class Generator() {
  def generateData(): String = {
    val event_types = List("close_courseware", "play_video", "click_about", "click_forum", "problem_check_incorrect", "create_thread", "seek_video",
      "problem_check_correct", "reset_problem", "create_comment", "load_video", "problem_get", "pause_video", "delete_comment", "problem_check",
      "click_courseware", "stop_video", "problem_save", "delete_thread", "click_progress", "click_info", "close_forum", "close_info")
    val date_event = DateTime.now
    val timestamp = date_event.getMillis

    val r = scala.util.Random
    val event = event_types(r.nextInt(event_types.size))
    val sample_row = s"{'sesion_id' : '${r.nextInt(100)}' , 'event_type':  '${event}', 'course_id': '${r.nextInt(100)}', 'event_time': '${date_event.toString("HH:mm:ss")}', 'user_id': '${r.nextInt(20000)}', 'event_place': '${event.split("_")(1)}', 'timestamp': '${timestamp.toString}', 'event_date': '${date_event.toString("yyyy-MM-dd")}'}"
    println(sample_row)
    sample_row
  }
}

object KafkaProducerApp extends App {
  val generator = new Generator()
  val props: Properties = new Properties()
  props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094")
  props.put("key.serializer",
    "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer",
    "org.apache.kafka.common.serialization.StringSerializer")
  props.put("acks", "all")
  val producer = new KafkaProducer[String, String](props)
  val topic = "text_topic"
  var counter = 0

  val argument = args.map(_.toInt)
  val maxRecord =  argument(0) // set if you want get limit record -> -1
  val intervalRecord = argument(1) // set with ns  -> -1
  val intervalFlushRecord = argument(2) // set with ms -> 100
  new Thread(() => {
    var last = System.nanoTime();
    var lastCounter = counter
    while (true) {
      val now = System.nanoTime();
      //      println((1.0 * now - 1.0 * last) / 1000)
      if ((1.0 * now - 1.0 * last) / 1000 > intervalFlushRecord) {
//        println("heree", counter - lastCounter)
        lastCounter = counter
        producer.flush()
        last = now
      }
    }
  }).start()
  try {
    breakable {
      while (true) {
        counter += 1
        val date_event = DateTime.now
        val timestamp = date_event.getMillis
        val generatedData = generator.generateData()
        println(s"record ${counter}")
        val record = new ProducerRecord[String, String](topic, counter.toString, generatedData)
        val metadata = producer.send(record)
        //        printf(s"sent record(key=%s value=%s) " +
        //          "meta(partition=%d, offset=%d)\n",
        //          record.key(), record.value(),
        //          metadata.get().partition(),
        //          metadata.get().offset())

        //        producer.flush()
        if (maxRecord != -1 && counter > maxRecord) {
          break
        }
        if (intervalRecord != -1 && intervalRecord > 0) {
          Thread.sleep(0, intervalRecord)
        }
      }
    }

  } catch {
    case e: Exception => e.printStackTrace()
  } finally {
    producer.close()
  }
}

// Callback trait only contains the one abstract method onCompletion
//private class compareProducerCallback extends Callback {
//  @Override
//  override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
//    if (exception != null) {
//      exception.printStackTrace()
//    }
//  }
//}
