package controllers.analytics

import cassandra.{ClicksPerEventRepo, OnlineUsersInCourseRepo, OnlineUsersRepo}
import play.api._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.Results._
import play.api.mvc._

import javax.inject._


@Singleton
class OnlineUserController @Inject()(
                                      cc: ControllerComponents,
                                      onlineUsersRepo: OnlineUsersRepo,
                                      onlineUsersInCourseRepo: OnlineUsersInCourseRepo,
                                      clicksPerEventRepo: ClicksPerEventRepo,
                                    ) extends AbstractController(cc) {

  implicit val LineChartWrite: Writes[LineChartResponse] = (
    (JsPath \ "count").write[Long] and
      (JsPath \ "date").write[Long]
    ) (unlift(LineChartResponse.unapply))

  implicit val PieChartWrite: Writes[PieChartResponse] = (
    (JsPath \ "count").write[Long] and
      (JsPath \ "event").write[String]
    ) (unlift(PieChartResponse.unapply))

  implicit val ReadOUR: Reads[SimpleRequest] = (
    (JsPath \ "startYear").read[Int] and
      (JsPath \ "startMonth").read[Int] and
      (JsPath \ "startDay").read[Int] and
      (JsPath \ "startHour").read[Int] and
      (JsPath \ "startMinute").read[Int] and
      (JsPath \ "endYear").read[Int] and
      (JsPath \ "endMonth").read[Int] and
      (JsPath \ "endDay").read[Int] and
      (JsPath \ "endHour").read[Int] and
      (JsPath \ "endMinute").read[Int]
    ) (SimpleRequest.apply _)

  implicit val ReadOURC: Reads[TempRequestForCourse] = (
    (JsPath \ "course").read[String] and
      (JsPath \ "startYear").read[Int] and
      (JsPath \ "startMonth").read[Int] and
      (JsPath \ "startDay").read[Int] and
      (JsPath \ "startHour").read[Int] and
      (JsPath \ "startMinute").read[Int] and
      (JsPath \ "endYear").read[Int] and
      (JsPath \ "endMonth").read[Int] and
      (JsPath \ "endDay").read[Int] and
      (JsPath \ "endHour").read[Int] and
      (JsPath \ "endMinute").read[Int]
    ) (TempRequestForCourse.apply _)

  implicit def TempToCourseReq(temp: TempRequestForCourse): SimpleRequestForCourse = {
    SimpleRequestForCourse(
      course = temp.course, simpleRequest = SimpleRequest(
        startYear = temp.startYear,
        startMonth = temp.startMonth,
        startDay = temp.startDay,
        startHour = temp.startHour,
        startMinute = temp.startMinute,
        endYear = temp.endYear,
        endMonth = temp.endMonth,
        endDay = temp.endDay,
        endHour = temp.endHour,
        endMinute = temp.endMinute
      )
    )
  }

  def onlineUsersInAnInterval(request: Request[AnyContent]) = {
    //      Cast body to json or get empty {}
    val json: JsValue = request.body.asJson.getOrElse(Json.parse("{}"))
    //      Validate json to be Event type.
    val req: JsResult[SimpleRequest] = json.validate[SimpleRequest]
    if (req.isError) {
      BadRequest("Not Event Type")
    }
    val ur = req.get
    val response = onlineUsersRepo.getOnlineUsersCount(ur)
    Ok(
      Json.obj(
        "data" -> Json.toJson(response)
      )
    )

  }

  def onlineUsersInAnCourseInAnInterval(request: Request[AnyContent]) = {
    //      Cast body to json or get empty {}
    val json: JsValue = request.body.asJson.getOrElse(Json.parse("{}"))
    //      Validate json to be Event type.
    val req: JsResult[TempRequestForCourse] = json.validate[TempRequestForCourse]
    if (req.isError) {
      BadRequest("Not Event Type")
    }

    val parsed: SimpleRequestForCourse = req.get
    val response = onlineUsersInCourseRepo.getOnlineUsersInCourseCount(parsed)
    Ok(
      Json.obj(
        "data" -> Json.toJson(response),
      )
    )
  }

  def clicksPerEvent(request: Request[AnyContent]) = {
    //      Cast body to json or get empty {}
    val json: JsValue = request.body.asJson.getOrElse(Json.parse("{}"))
    //      Validate json to be Event type.
    val req: JsResult[SimpleRequest] = json.validate[SimpleRequest]
    if (req.isError) {
      BadRequest("Not Event Type")
    }

    val parsed = req.get
    val response = clicksPerEventRepo.getClicksPerEventCount(parsed)
    Ok(
      Json.obj(
        "data" -> Json.toJson(response),
      )
    )
  }

}


case class SimpleRequest(
                          startYear: Int,
                          startMonth: Int,
                          startDay: Int,
                          startHour: Int,
                          startMinute: Int,
                          endYear: Int,
                          endMonth: Int,
                          endDay: Int,
                          endHour: Int,
                          endMinute: Int,
                        )

case class SimpleRequestForCourse(
                                   course: String,
                                   simpleRequest: SimpleRequest
                                 )

case class TempRequestForCourse(
                                 course: String,
                                 startYear: Int,
                                 startMonth: Int,
                                 startDay: Int,
                                 startHour: Int,
                                 startMinute: Int,
                                 endYear: Int,
                                 endMonth: Int,
                                 endDay: Int,
                                 endHour: Int,
                                 endMinute: Int,
                               )

case class LineChartResponse(count: Long, date: Long)

case class PieChartResponse(count: Long, event: String)