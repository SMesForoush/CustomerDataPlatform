
import com.github.nscala_time.time.Imports.DateTime
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties

object KafkaProducerApp extends App {

  val props: Properties = new Properties()
  props.put("bootstrap.servers", "localhost:29092")
  props.put("key.serializer",
    "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer",
    "org.apache.kafka.common.serialization.StringSerializer")
  props.put("acks", "all")
  val producer = new KafkaProducer[String, String](props)
  val topic = "text_topic"
  try {
    for (i <- 0 to 15) {
      val date_event = DateTime.now
      val timestamp = date_event.getMillis
      val record = new ProducerRecord[String, String](topic, i.toString, timestamp.toString)
      val metadata = producer.send(record)
      printf(s"sent record(key=%s value=%s) " +
        "meta(partition=%d, offset=%d)\n",
        record.key(), record.value(),
        metadata.get().partition(),
        metadata.get().offset())
    }
  } catch {
    case e: Exception => e.printStackTrace()
  } finally {
    producer.close()
  }
}
