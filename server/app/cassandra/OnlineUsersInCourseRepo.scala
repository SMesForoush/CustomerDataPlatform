package cassandra

import com.datastax.oss.driver.api.core.cql._
import com.datastax.oss.driver.api.querybuilder.QueryBuilder.{bindMarker, selectFrom}
import controllers.analytics.OnlineUserCourseRequest

import javax.inject.Inject
import scala.jdk.CollectionConverters._
import scala.language.implicitConversions

class OnlineUsersInCourseRepo @Inject()(cassandraService: CassandraService) {

  def getOnlineUsersInCourseCount(request: OnlineUserCourseRequest) = {
    val result = cassandraService.useSession { session =>
      val resultSet = session.execute(SimpleStatement.builder(
        s"SELECT count(user_id) FROM myorg.online_users_by_course_time WHERE course_id=${course_id} AND event_date=${event_date};"
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


