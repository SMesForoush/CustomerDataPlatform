package cassandra

import com.datastax.oss.driver.api.core.cql._
import controllers.analytics.{PieChartResponse, SimpleRequest}

import javax.inject.Inject
import scala.jdk.CollectionConverters._
import scala.language.implicitConversions

class ClicksPerEventRepo @Inject()(cassandraService: CassandraService) {

  def getClicksPerEventCount(onlineUserRequest: SimpleRequest): List[PieChartResponse] = {
    val start = onlineUserRequest.start
    val end = onlineUserRequest.end
    val result = cassandraService.useSession[List[PieChartResponse]] { session =>
      val resultSet = session.execute(SimpleStatement.builder(
        "" // TODO INJA QUERY RO VARED KON
      ).build())

      val resultAsList = resultSet.asScala
      resultAsList.map(rowToValue(_)).toList
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


