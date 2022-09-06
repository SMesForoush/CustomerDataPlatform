package cassandra

import com.datastax.oss.driver.api.core.cql._
import controllers.analytics.{LineChartResponse, SimpleRequest}

import javax.inject.Inject
import scala.jdk.CollectionConverters._
import scala.language.implicitConversions

class OnlineUsersRepo @Inject()(cassandraService: CassandraService) {

  def getOnlineUsersCount(onlineUserRequest: SimpleRequest): List[LineChartResponse] = {
    val start = onlineUserRequest.start
    val end = onlineUserRequest.end
    val query = s"SELECT count(user_id) as count, event_date as date FROM online_users WHERE event_date<'${end}' AND event_date>'${start}' GROUP BY event_date ALLOW FILTERING;"
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
      // throw exception
   //   println(row.isNull(countColName))
     // println(row.isNull(eventDateColName))
      LineChartResponse(row.getLong(countColName), row.getLocalDate(eventDateColName).toString())
    } else {
    LineChartResponse(row.getLong(countColName), row.getLocalDate(eventDateColName).toString())
    }
  }
}


