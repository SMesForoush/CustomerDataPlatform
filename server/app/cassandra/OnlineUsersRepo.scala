package cassandra

import com.datastax.oss.driver.api.core.cql._
import controllers.analytics.{SimpleRequest, LineChartResponse}

import javax.inject.Inject
import scala.jdk.CollectionConverters._
import scala.language.implicitConversions

class OnlineUsersRepo @Inject()(cassandraService: CassandraService) {

  def getOnlineUsersCount(onlineUserRequest: SimpleRequest): List[LineChartResponse] = {
    val (dateCondition, date) = cassandraService.queryOnDate(onlineUserRequest)
    val query = s"SELECT count(user_id) as count, ${date} as date FROM online_users WHERE ${dateCondition} GROUP BY ${date} ALLOW FILTERING;"
    println(query)
    val result = cassandraService.useSession[List[LineChartResponse]] { session =>
      val resultSet = session.execute(SimpleStatement.builder(
        query
      ).build())
      try {
        val resultAsList = resultSet.asScala
        resultAsList.map(rowToValue(_)).toList
      } catch {
        case x: Exception => List[LineChartResponse]()
      }
    }
    result
  }

  private implicit def rowToValue(row: Row) = {
    val countColName = "count";
    val eventDateColName = "date";
    if (row.isNull(countColName) || row.isNull(eventDateColName)) {
      LineChartResponse(row.getLong(countColName), row.getLong(eventDateColName))
    } else {
      LineChartResponse(row.getLong(countColName), row.getLong(eventDateColName))
    }
  }
}


