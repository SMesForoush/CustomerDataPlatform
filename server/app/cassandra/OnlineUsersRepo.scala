package cassandra

import com.datastax.oss.driver.api.core.cql._
import controllers.analytics.{SimpleRequest, LineChartResponse}

import javax.inject.Inject
import scala.jdk.CollectionConverters._
import scala.language.implicitConversions

class OnlineUsersRepo @Inject()(cassandraService: CassandraService) {

  def getOnlineUsersCount(onlineUserRequest: SimpleRequest): List[LineChartResponse] = {
    val start = onlineUserRequest.start
    val end = onlineUserRequest.end
    val result = cassandraService.useSession[List[LineChartResponse]] { session =>
      val resultSet = session.execute(SimpleStatement.builder(
        s"SELECT event_date as date, count(user_id) as count FROM online_users WHERE (${start} < event_date AND ${end} > event_date) GROUP BY event_date, event_time;"
      ).build())

      val resultAsList = resultSet.asScala
      resultAsList.map(rowToValue(_)).toList
    }
    result
  }

  private implicit def rowToValue(row: Row) = {
    val countColName = "count";
    val eventDateColName = "date";
    if (row.isNull(countColName) || row.isNull(eventDateColName)) {
      // throw exception
    }
    LineChartResponse(row.getLong(countColName), row.getString(eventDateColName))
  }
}


