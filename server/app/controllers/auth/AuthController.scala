package controllers.auth

import jwt.{JWTService, UserInfo}
import play.api.data.Forms._
import play.api.data._
import play.api.http.HeaderNames
import play.api.mvc._

import javax.inject._

@Singleton
class AuthController @Inject()(cc: ControllerComponents, jwtService: JWTService) extends AbstractController(cc) {
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
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user => {
        val token = jwtService.encodeJWT(getData(user))
        Redirect("/").withHeaders(
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

