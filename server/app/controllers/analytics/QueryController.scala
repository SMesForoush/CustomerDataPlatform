package controllers.analytics;

import jwt.{JWTAction, LoginRequiredAction, UserRequest}
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import cassandra._
import javax.inject._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._

@Singleton
class QueryController @Inject()(
                                cc: ControllerComponents,
                                config: Configuration,
                                // jwtAction: JWTAction,
                                loginRequiredAction: LoginRequiredAction,
                                // dfRepo: DfRepo,
                                // myRepo: MyRepo,
                                onlineUserController: OnlineUserController,
                                onlineUsersRepo: OnlineUsersRepo
                              ) extends AbstractController(cc) {

  // TODO: use loginRequiredAction for all
  def getOnlineUsers() = Action { implicit request: Request[AnyContent] =>
    // val result = onlineUsersRepo.getOnlineUsersCount()
    // Ok(Json.toJson(result))
    
    onlineUserController.onlineUsersInAnInterval(request)
  }

  def getOnlineUsersByCourseTime() = loginRequiredAction { implicit request: Request[AnyContent] =>
    Ok("Not implemented")
  }

  def getClickByPlace(place: String) = Action { implicit request: Request[AnyContent] =>
    Ok("Hello" + place)
  }

  // implicit val EventReads: Reads[Event] = (
  //   (JsPath \ "sessionId").read[String] and
  //     (JsPath \ "courseId").read[String] and
  //     (JsPath \ "userId").read[String] and
  //     (JsPath \ "createdAt").read[String] and
  //     (JsPath \ "name").read[String]
  //   ) (Event.apply _)
}

// case class DateTime(date: String, time: String)