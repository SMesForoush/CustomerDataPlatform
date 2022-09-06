package cassandra

import com.datastax.oss.driver.api.core.CqlSession
import controllers.analytics.SimpleRequest
import play.api.Configuration

import java.net.{InetSocketAddress, URL}
import javax.inject.{Inject, Singleton}
import scala.jdk.CollectionConverters._


@Singleton()
class CassandraService @Inject()(config: Configuration) {

  private def username = config.get[String]("cassandra.username")

  private def keyspace = config.get[String]("cassandra.keyspace")

  private def password = config.get[String]("cassandra.password")

  private def contactPoints = config.get[Seq[String]]("cassandra.contact-points")
    .map(url => new URL(url))
    .map(url => new InetSocketAddress(url.getHost, url.getPort))

  private val cqlSession: CqlSession =
    CqlSession.builder()
      .addContactPoints(contactPoints.asJava)
      .withKeyspace(keyspace)
      .withAuthCredentials(username, password)
      .withLocalDatacenter("datacenter1")
      .build()

  def queryOnDate(date: SimpleRequest): (String, String) = {
    var condition = ""
    if (date.startYear == date.endYear) {
      condition += s" year=${date.endYear}"
    } else {
      condition += s" year>${date.startYear} AND year<${date.endYear}"
      return (condition, "year")
    }
    if (date.startMonth == date.endMonth) {
      condition += s" AND month=${date.startMonth}"
    } else {
      condition += s" AND month>${date.startMonth} AND month<${date.startMonth}"
      return (condition, "month")
    }
    if (date.startDay == date.endDay) {
      condition += s" AND day=${date.startDay}"
    } else {
      condition += s" AND day>${date.startDay} AND day<${date.endDay}"
      return (condition, "day")
    }
    if (date.startHour == date.startHour) {
      condition += s" AND hour=${date.startHour}"
    } else {
      condition += s" AND hour>${date.startHour} AND hour<${date.endHour}"
      return (condition, "hour")
    }
    condition += s" AND minute>${date.startMinute} AND minute<${date.endMinute}"
    (condition, "minute")
  }


  def useSession[A](f: CqlSession => A): A = {
    val session = cqlSession
    val result = f(session)
    result
  }

}
