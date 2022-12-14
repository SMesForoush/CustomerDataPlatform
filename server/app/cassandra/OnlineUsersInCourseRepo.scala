package cassandra

import com.datastax.oss.driver.api.core.cql._
import controllers.analytics.{SimpleRequestForCourse, LineChartResponse}

import javax.inject.Inject
import scala.jdk.CollectionConverters._
import scala.language.implicitConversions

class OnlineUsersInCourseRepo @Inject()(cassandraService: CassandraService) {

  def getOnlineUsersInCourseCount(request: SimpleRequestForCourse): List[LineChartResponse] = {
    val result = cassandraService.useSession { session =>
      val course_id = request.course
      val start = request.start
      val end = request.end
      val query = s"SELECT count(user_id) as count, event_date as date FROM online_users_by_course_time WHERE course_id='${course_id}' AND event_date<'${end}' AND event_date>'${start}' GROUP BY event_date, course_id ALLOW FILTERING;"
      println(query)
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
    val countCol = "count"
    val dateCol = "date"
    if (row.isNull(countCol) || row.isNull(dateCol)) {
      // throw exception
    }
    LineChartResponse(row.getLong(countCol), row.getLocalDate(dateCol).toString())
  }
}


