package autenticacion.models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class User(name: String, role:String) {
  def isAdmin: Boolean = (name.toLowerCase == "admin")
}

object User {
  implicit val userFormat = Json.format[User]
}
