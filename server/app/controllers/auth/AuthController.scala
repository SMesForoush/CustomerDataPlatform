package controllers.auth

import jwt.{JWTAction, JWTService, UserInfo, UserRequest}
import play.api.data.Forms._
import play.api.data._
import play.api.http.HeaderNames
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._

@Singleton
class AuthController @Inject()(cc: ControllerComponents, jwtService: JWTService, jwtAction: JWTAction) extends AbstractController(cc) {
  val loginForm: Form[(String, String)] = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying("Invalid email or password", result => result match {
      case (email, password) => check(email, password)
    })
  )

  def check(email: String, password: String) = {
    (email == "admin@admin.com" && password == "1234")
  }

  //  Implement repository to get user info from db
  def getData(loginForm: (String, String)) = UserInfo(loginForm._1, "Amir")

  def authenticate = Action { implicit request: Request[AnyContent] =>
    loginForm.bindFromRequest().fold(
      formWithErrors => BadRequest(Json.obj(
        "errors" -> formWithErrors.errors.map(_.message)
          .reduce[String](
            (a: String, b: String) => s"$a , $b"),
        "hasError" -> true
      )),
      user => {
        val userData = getData(user)
        val token = jwtService.encodeJWT(userData)
        Ok(Json.obj
        ("token" -> token,
          "email" -> userData.email,
          "name" -> userData.name
        )).withHeaders(
          (
            HeaderNames.SET_COOKIE,
            s"${HeaderNames.AUTHORIZATION}=${token}"
          ),
          (
            HeaderNames.AUTHORIZATION,
            token
          )
        )
      }
    )
  }

  def validate = jwtAction { implicit request: UserRequest[AnyContent] =>
    val success = request.user match {
      case None => false
      case _ => true
    };
    Ok(Json.obj(
      "success" -> success
    ))
  }

  def login = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login(loginForm))
  }

  def logout = Action {
    Redirect("/").withHeaders(
      (
        HeaderNames.SET_COOKIE,
        s"${HeaderNames.AUTHORIZATION}="
      ),
    )
  }

}

