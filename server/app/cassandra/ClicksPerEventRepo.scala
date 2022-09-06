package cassandra

import com.datastax.oss.driver.api.core.cql._
import controllers.analytics.{PieChartResponse, SimpleRequest}

import javax.inject.Inject
import scala.jdk.CollectionConverters._
import scala.language.implicitConversions

class ClicksPerEventRepo @Inject()(cassandraService: CassandraService) {

  def getClicksPerEventCount(onlineUserRequest: SimpleRequest): List[PieChartResponse] = {
    val (conditionDate, date) = cassandraService.queryOnDate(onlineUserRequest)
    val query = s"SELECT count(*) as count, event_place as event FROM click_by_place WHERE ${conditionDate} GROUP BY event_place, ${date} ALLOW FILTERING;"
    println(query)
    val result = cassandraService.useSession[List[PieChartResponse]] { session =>
      val resultSet = session.execute(SimpleStatement.builder(
        query
      ).build())
    try {
      val resultAsList = resultSet.asScala
      resultAsList.map(rowToValue(_)).toList
    } catch {
      case x: Exception => List[PieChartResponse]()
    }


    }
    result
  }

  private implicit def rowToValue(row: Row) = {
    val countColName = "count";
    val eventColName = "event";
    if (row.isNull(countColName) || row.isNull(eventColName)) {
      // throw exception
    }
    PieChartResponse(row.getLong(countColName), row.getString(eventColName))
  }
}


