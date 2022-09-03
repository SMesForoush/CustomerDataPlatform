package cassandra

import com.datastax.oss.driver.api.core.cql._
import com.datastax.oss.driver.api.querybuilder.QueryBuilder.{bindMarker, selectFrom}

import javax.inject.Inject
import scala.jdk.CollectionConverters._
import scala.language.implicitConversions

class OnlineUsersRepo @Inject()(cassandraService: CassandraService) {

  def getOnlineUsersCount(fromDate: String, fromTime: String, toDate: String, toTime: String) = {
    val result = cassandraService.useSession[List[(String, Int)]] { session =>
      val resultSet = session.execute(SimpleStatement.builder(
        s"SELECT event_date,count(user_id) FROM myorg.online_users WHERE (${fromDate} < event_date AND ${toDate} > event_date) GROUP BY event_date, event_time;"
      ).build())

      val resultAsList = resultSet.asScala
      resultAsList.map(rowToValue(_)).toList
    }
    result
  }

  private implicit def rowToValue(row: Row) = {
    val countColName = "system.count(user_id)";
    var eventDateColName = "event_date";
    if (row.isNull(countColName)) {
      // throw exception
    }
    (row.getString(eventDateColName), row.getInt(countColName))
  }
}


