package controllers.analytics

import cassandra.OnlineUsersRepo
import play.api._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.Results._
import play.api.mvc._

import javax.inject._


@Singleton
class OnlineUserController @Inject()(
                                      cc: ControllerComponents,
                                      onlineUsersRepo: OnlineUsersRepo
                                    ) extends AbstractController(cc) {

  implicit val ReadOUR: Reads[OnlineUserRequest] = (
    (JsPath \ "fromDate").read[String] and
      (JsPath \ "fromTime").read[String] and
      (JsPath \ "toDate").read[String] and
      (JsPath \ "toTime").read[String]
    ) (OnlineUserRequest.apply _)

  implicit val ReadOUCR: Reads[OnlineUserCourseRequest] = (
    (JsPath \ "course").read[String] and
      (JsPath \ "fromDate").read[String] and
      (JsPath \ "fromTime").read[String] and
      (JsPath \ "toDate").read[String] and
      (JsPath \ "toTime").read[String]
    ) (OnlineUserCourseRequest.apply _)


  def onlineUsersInAnInterval(request: Request[AnyContent]) = {
    //      Cast body to json or get empty {}
    val json: JsValue = request.body.asJson.getOrElse(Json.parse("{}"))
    //      Validate json to be Event type.
    val req: JsResult[OnlineUserRequest] = json.validate[OnlineUserRequest]
    if (req.isError) {
      BadRequest("Not Event Type")
    } else {
      val ur = req.get
      val response = onlineUsersRepo.getOnlineUsersCount(ur.fromDate, ur.fromTime, ur.toDate, ur.toTime)
      Ok(Json.toJson(response))
    }
  }

  def onlineUsersInAnCourseInAnInterval() = Action { implicit request: Request[AnyContent] =>
    //      Cast body to json or get empty {}
    val json: JsValue = request.body.asJson.getOrElse(Json.parse("{}"))
    //      Validate json to be Event type.
    val req: JsResult[OnlineUserCourseRequest] = json.validate[OnlineUserCourseRequest]
    if (req.isError) {
      BadRequest("Not Event Type")
    } else {
      req.get
      Ok("created")
    }
  }
}

case class OnlineUserRequest(fromDate: String, fromTime: String, toDate: String, toTime: String)
case class OnlineUserCourseRequest(course: String, fromDate: String, fromTime: String, toDate: String, toTime: String)