package cassandra

import com.datastax.oss.driver.api.core.cql._
import com.datastax.oss.driver.api.querybuilder.QueryBuilder.{bindMarker, selectFrom}

import javax.inject.Inject
import scala.jdk.CollectionConverters._
import scala.language.implicitConversions

class OnlineUsersRepo @Inject()(cassandraService: CassandraService) {

  def getOnlineUsersCount() = {
    val result = cassandraService.useSession { session =>
      val resultSet = session.execute(SimpleStatement.builder(
        "SELECT count(user_id) FROM myorg.online_users GROUP BY event_date, event_time;"
      ).build())

      val resultAsList = resultSet.asScala
      resultAsList.map(rowToValue(_)).toList
    }
    result
  }

  private implicit def rowToValue(row: Row) = {
    val countColName = "system.count(user_id)";
    if (row.isNull(countColName)) {
      // throw exception
    }
    row.getLong(countColName)
  }
}


