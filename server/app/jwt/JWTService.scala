package jwt

import pdi.jwt.{JwtAlgorithm, JwtJson}
import play.api.Configuration
import play.api.libs.functional.syntax._
import play.api.libs.json._

import javax.inject.Inject

case class TokenClass(user: UserInfo)

class JWTService @Inject()(config: Configuration) {

  private def secretKey = config.get[String]("auth.secret")
  private val algo = JwtAlgorithm.HS256
  def validateJwt(token: String): Boolean = !decodeJWT(token).isEmpty

  def decodeJWT(token: String): Option[UserInfo] = {
    val tryJson = JwtJson.decodeJson(token, secretKey, Seq(algo))
    if (tryJson.isFailure) {
      return Option.empty[UserInfo]
    }
    val json = tryJson.get
    val user = (json \ "user").validate[UserInfo]
    if (user.isError) {
      return Option.empty[UserInfo]
    }
    Option(user.get)
  }

  implicit val formatUser = Json.format[UserInfo]
  implicit val UserValidate: Reads[UserInfo] = (
    (JsPath \ "email").read[String] and
      (JsPath \ "name").read[String]
    ) (UserInfo.apply _)

  def encodeJWT(user: UserInfo): String =
    JwtJson.encode(Json.obj(("user", user)), secretKey, algo)
}

case class UserInfo(email: String, name: String)
