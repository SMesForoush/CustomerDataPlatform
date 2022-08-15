package controllers

import play.api._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._

import javax.inject._


@Singleton
class EventController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {


  implicit val EventWrites = new Writes[Event] {
    def writes(event: Event) = Json.obj(
      "sessionId" -> event.sessionId,
      "userId" -> event.userId,
      "courseId" -> event.courseId,
      "createdAt" -> event.createdAt,
    )
  }

  implicit val EventReads: Reads[Event] = (
    (JsPath \ "sessionId").read[String] and
      (JsPath \ "courseId").read[String] and
      (JsPath \ "userId").read[String] and
      (JsPath \ "createdAt").read[String] and
      (JsPath \ "name").read[String]
    ) (Event.apply _)

  def create() = Action { implicit request: Request[AnyContent] =>
    println("get")

    //      Cast body to json or get empty {}
    val json: JsValue = request.body.asJson.getOrElse(Json.parse("{}"))
    //      Validate json to be Event type.
    val event: JsResult[Event] = json.validate[Event]
    if (event.isError) {
      BadRequest("Not Event Type")
    } else {
      println(event.get)
      //    println(jso)
      Ok("created")
    }


  }
}


case class Event(sessionId: String, courseId: String, userId: String, createdAt: String, name: String)