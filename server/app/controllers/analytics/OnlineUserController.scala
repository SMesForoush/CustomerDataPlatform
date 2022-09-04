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
      (JsPath \ "date").write[String]
    ) (unlift(LineChartResponse.unapply))

  implicit val PieChartWrite: Writes[PieChartResponse] = (
    (JsPath \ "count").write[Long] and
      (JsPath \ "event").write[String]
    ) (unlift(PieChartResponse.unapply))

  implicit val ReadOUR: Reads[SimpleRequest] = (
    (JsPath \ "start").read[String] and
      (JsPath \ "end").read[String]
    ) (SimpleRequest.apply _)

  implicit val ReadOUCR: Reads[SimpleRequestForCourse] = (
    (JsPath \ "course").read[String] and
      (JsPath \ "start").read[String] and
      (JsPath \ "end").read[String]
    ) (SimpleRequestForCourse.apply _)


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
    Ok(Json.toJson(response))

  }

  def onlineUsersInAnCourseInAnInterval(request: Request[AnyContent]) = {
    //      Cast body to json or get empty {}
    val json: JsValue = request.body.asJson.getOrElse(Json.parse("{}"))
    //      Validate json to be Event type.
    val req: JsResult[SimpleRequestForCourse] = json.validate[SimpleRequestForCourse]
    if (req.isError) {
      BadRequest("Not Event Type")
    }

    val parsed = req.get
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


case class SimpleRequest(start: String, end: String)

case class SimpleRequestForCourse(start: String, end: String, course: String)

case class LineChartResponse(count: Long, date: String)

case class PieChartResponse(count: Long, event: String)