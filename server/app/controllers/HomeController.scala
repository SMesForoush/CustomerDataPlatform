package controllers

import jwt.{JWTAction, LoginRequiredAction, UserRequest}
import play.api._
import play.api.mvc._

import javax.inject._

@Singleton
class HomeController @Inject()(cc: ControllerComponents, config: Configuration, jwtAction: JWTAction, loginRequiredAction: LoginRequiredAction) extends AbstractController(cc) {

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
    Ok(views.html.tutorial())
  }

  def hello(name: String) = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.hello(name))
  }

}
