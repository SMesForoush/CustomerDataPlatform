package controllers.analytics;

import cassandra._
import jwt.{JWTAction, LoginRequiredAction, UserRequest}
import play.api._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._

import javax.inject._

@Singleton
class QueryController @Inject()(
                                 cc: ControllerComponents,
                                 config: Configuration,
                                 jwtAction: JWTAction,
                                 loginRequiredAction: LoginRequiredAction,
                                 dfRepo: DfRepo,
                                 // myRepo: MyRepo,
                                 onlineUserController: OnlineUserController,
                                 onlineUsersRepo: OnlineUsersRepo
                               ) extends AbstractController(cc) {

  def getOnlineUsers() = loginRequiredAction { implicit request: UserRequest[AnyContent] =>
    onlineUserController.onlineUsersInAnInterval(request.request)
  }

  def getOnlineUsersByCourseTime() = loginRequiredAction { implicit request: UserRequest[AnyContent] =>
    onlineUserController.onlineUsersInAnCourseInAnInterval(request.request)
  }

  def getClickPerEvent() = loginRequiredAction { implicit request: UserRequest[AnyContent] =>
    onlineUserController.clicksPerEvent(request.request)
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