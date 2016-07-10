package usuariosApp.modelo

import org.joda.time.DateTime
import java.sql.Timestamp
import slick.driver.MySQLDriver.api._

case class UsuarioApp(
   username: String,
   nombre: String,
   password: String,
   role:String)


class UsuarioAppTable(tag: Tag) extends Table[UsuarioApp](tag, "usuariosapp") {
  def username = column[String]("username")
  def nombre = column[String]("nombre")
  def password = column[String]("password")
  def role = column[String]("role")
 override def * = (username, nombre, password, role) <>(UsuarioApp.tupled, UsuarioApp.unapply _)
}





