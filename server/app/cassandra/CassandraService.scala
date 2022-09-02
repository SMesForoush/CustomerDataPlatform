package cassandra

import com.datastax.oss.driver.api.core.CqlSession
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


  def useSession[A](f: CqlSession => A): A = {
    val session = cqlSession
    val result = f(session)
    session.close()
    result
  }

}
