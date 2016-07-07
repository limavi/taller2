package migrana.modelo

import org.joda.time.DateTime
import java.sql.Timestamp
import slick.driver.MySQLDriver.api._

case class Paciente(
   Id: Long,
   TipoDocumento: String,
   NumeroDocumento: Long,
   Nombres: String,
   Apellidos: String,
   FechaNacimiento: DateTime,
   Sexo: String)

class PacienteTable(tag: Tag) extends Table[Paciente](tag, "paciente") {
  implicit val jdateColumnType = MappedColumnType.base[ DateTime, Timestamp ]( dt => new Timestamp( dt.getMillis ), ts => new DateTime( ts.getTime ) )
  def Id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def TipoDocumento = column[String]("TipoDocumento")
  def NumeroDocumento = column[Long]("NumeroDocumento")
  def Nombres = column[String]("Nombres")
  def Apellidos = column[String]("Apellidos")
  def FechaNacimiento = column[ DateTime ]( "FechaNacimiento")
  def Sexo = column[String]("Sexo")
  override def * = (Id, TipoDocumento, NumeroDocumento, Nombres, Apellidos,FechaNacimiento,Sexo) <>(Paciente.tupled, Paciente.unapply _)
}


