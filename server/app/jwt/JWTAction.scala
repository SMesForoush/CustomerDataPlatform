package jwt

import play.api.http.HeaderNames
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

case class UserRequest[A](user: Option[UserInfo], token: Option[String], request: Request[A]) extends WrappedRequest[A](request)


class JWTAction @Inject()(bodyParser: BodyParsers.Default, jwtService: JWTService)(implicit ec: ExecutionContext) extends ActionBuilder[UserRequest, AnyContent] {

  override def parser: BodyParser[AnyContent] = bodyParser

  override protected def executionContext: ExecutionContext = ec

  override def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] = {
    val optionUserRequest = getUserRequest(request)
    if (optionUserRequest.isEmpty) {
      return Future.successful(Results.Unauthorized.withHeaders(
        (
          HeaderNames.SET_COOKIE,
          s"${HeaderNames.AUTHORIZATION}="
        ),
      ))
    }
    block(
      optionUserRequest.get
    )
  }


  def getUserRequest[A](request: Request[A]): Option[UserRequest[A]] = {
    val cookieToken = request.cookies.get(HeaderNames.AUTHORIZATION)
    if (cookieToken.isEmpty || cookieToken.get.value.length == 0) {
      return Option(UserRequest(Option.empty[UserInfo], Option.empty[String], request))
    }
    //     todo implement expire
    val token = cookieToken.get.value
    val isValidToken = jwtService.validateJwt(token)
    if (!isValidToken) {
      return Option.empty[UserRequest[A]]

    }
    Option(UserRequest(jwtService.decodeJWT(token), Option(token), request))

  }

}

class LoginRequiredAction @Inject()(bodyParser: BodyParsers.Default, jwtService: JWTService)(implicit ec: ExecutionContext) extends ActionBuilder[UserRequest, AnyContent] {

  override def parser: BodyParser[AnyContent] = bodyParser

  override protected def executionContext: ExecutionContext = ec

  override def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] = {
    val jwtAction = new JWTAction(bodyParser, jwtService)
    val optionUserRequest = jwtAction.getUserRequest(request)
    if (optionUserRequest.isEmpty) {
      return Future.successful(Results.Unauthorized.withHeaders(
        (
          HeaderNames.SET_COOKIE,
          s"${HeaderNames.AUTHORIZATION}="
        ),
      ))
    }
    else if (optionUserRequest.get.user.isEmpty) {
      return Future.successful(Results.Forbidden.withHeaders(
        (
          HeaderNames.SET_COOKIE,
          s"${HeaderNames.AUTHORIZATION}="
        ),
      ))
    }
    block (
      optionUserRequest.get
    )
  }
}
