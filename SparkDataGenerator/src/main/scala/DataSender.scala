//import com.github.nscala_time.time.Imports.DateTime
import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class DataSender extends App{
  val props: Properties = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer",
    "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer",
    "org.apache.kafka.common.serialization.StringSerializer")
  props.put("acks", "all")
  def generateData(): String = {
    val event_types = List("close_courseware", "play_video", "click_about", "click_forum", "problem_check_incorrect", "create_thread", "seek_video",
      "problem_check_correct", "reset_problem", "create_comment", "load_video", "problem_get", "pause_video", "delete_comment", "problem_check",
      "click_courseware", "stop_video", "problem_save", "delete_thread", "click_progress", "click_info", "close_forum", "close_info")
//    val date_event = DateTime.now
//    val timestamp = date_event.getMillis

    val r = scala.util.Random
    val event = event_types(r.nextInt(event_types.size))
    val sample_row = s"{'sesion_id' : '${r.nextInt(100)}' , 'event_type':  '${event}', 'course_id': '${r.nextInt(100)}', 'event_time': '${date_event.toString("HH:mm:ss")}', 'user_id': '${r.nextInt(20000)}', 'event_place': '${event.split("_")(1)}', 'timestamp': '${timestamp.toString}', 'event_date': '${date_event.toString("yyyy-MM-dd")}'"
    println(sample_row)
    sample_row
  }


}
