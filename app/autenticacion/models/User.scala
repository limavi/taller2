package autenticacion.models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class User(name: String, role:String) {

  def isAdmin:    Boolean = (role.toLowerCase == "admin")
  def isMedico:   Boolean = (role.toLowerCase == "medico")
  def isPaciente: Boolean = (role.toLowerCase == "paciente")
}

object User {
  implicit val userFormat = Json.format[User]
}
