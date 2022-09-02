package cassandra

import com.datastax.oss.driver.api.core.cql._
import com.datastax.oss.driver.api.querybuilder.QueryBuilder.{bindMarker, selectFrom}

import javax.inject.Inject
import scala.jdk.CollectionConverters._
import scala.language.implicitConversions

class DfRepo @Inject()(cassandraService: CassandraService) {
  private val table = "df"

  def getDfByWord(word: String): List[DfWord] = {
    val words = cassandraService.useSession[List[DfWord]] { session =>
      val selectQuery = selectFrom(table)
        .columns("word", "df")
        .whereColumn("word")
        .isEqualTo(bindMarker("wordEqual"))
      //          literal(4)
      //    -> another way to set value like bindMaker

      val preparedQuery: PreparedStatement = session.prepare(selectQuery.build())
      val statement = preparedQuery.bind(word)
      val resultSet = session.execute(statement).iterator()
      if (!resultSet.hasNext) return List[DfWord]()
      //     may be exception


      val resultAsList = resultSet.asScala
      val words = listRowToListClass(resultAsList.toList)
      return words
      //    I try to make a better sample but you just pass resultSet.one() for get one object

    }
    words
  }

  private implicit def rowToClass(row: Row) = {
    if (row.isNull("df") || row.isNull("word")) {
      //       throw a exception
    }
    DfWord(df = row.getInt("df"), word = row.getString("word"))
  }

  private implicit def listRowToListClass(rows: List[Row]) = rows.map(rowToClass(_))
}

case class DfWord(df: Int, word: String)
