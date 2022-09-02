package controllers

import cassandra.DfRepo
import jwt.{JWTAction, LoginRequiredAction, UserRequest}
import play.api._
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._

@Singleton
class HomeController @Inject()(
                                cc: ControllerComponents,
                                config: Configuration,
                                jwtAction: JWTAction,
                                loginRequiredAction: LoginRequiredAction,
                                dfRepo: DfRepo
                              ) extends AbstractController(cc) {

  def index() = jwtAction { implicit request: UserRequest[AnyContent] =>
    val username = request.user map {
      case user => user.name
    } getOrElse "unknown"
    Ok(views.html.index(username))
  }

  def explore() = loginRequiredAction { implicit request: Request[AnyContent] =>
    Ok(views.html.explore())
  }

  def tutorial() = Action { implicit request: Request[AnyContent] =>
    val start = System.nanoTime
    println(dfRepo.getDfByWord("hello"))
    val end = System.nanoTime
    println((end - start) / 1000)
    Ok(views.html.tutorial())
  }

  def hello(name: String) = Action { implicit request: Request[AnyContent] =>
    val res = Json.obj(
      "sessionId" -> "session",
      "userId" -> name,
      "courseId" -> "wow",
      "createdAt" -> "be to che",
    )
    //    Ok(views.html.hello(name))
    Ok(res)
  }

  def sampleQuery() = Action { implicit request: Request[AnyContent] =>
    Ok("Hello, your result is " + dfRepo.getDfByWord("hello"))
  }

}
