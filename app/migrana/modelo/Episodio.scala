package migrana.modelo

import org.joda.time.DateTime
import java.sql.Timestamp
import slick.driver.MySQLDriver.api._

case class Episodio(
   Id: Long,
IdPaciente:Long,
FechayHoraEpisodio: DateTime,
NivelDolor: String,
LocalizacionDolor:String,
PromedioHorasSuenoSemanal:Long,
MedicamentoTomaActualmente: String,
BebidasqueCausaronMigrana: String,
AlimentosqueCausaronMigrana: String,
ActividadFisicaqueCausaronMigrana: String)


class EpisodioTable(tag: Tag) extends Table[Episodio](tag, "episodio") {
  implicit val jdateColumnType = MappedColumnType.base[ DateTime, Timestamp ]( dt => new Timestamp( dt.getMillis ), ts => new DateTime( ts.getTime ) )
  def Id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def IdPaciente = column[Long]("IdPaciente")
  def FechayHoraEpisodio = column[ DateTime ]( "FechayHoraEpisodio")
  def NivelDolor = column[String]("NivelDolor")
  def LocalizacionDolor = column[String]("LocalizacionDolor")
  def PromedioHorasSuenoSemanal = column[Long]("PromedioHorasSuenoSemanal")
  def MedicamentoTomaActualmente = column[String]("MedicamentoTomaActualmente")
  def BebidasqueCausaronMigrana = column[String]("BebidasqueCausaronMigrana")
  def AlimentosqueCausaronMigrana = column[String]("AlimentosqueCausaronMigrana")
  def ActividadFisicaqueCausaronMigrana = column[String]("ActividadFisicaqueCausaronMigrana")
  override def * = (Id, IdPaciente, FechayHoraEpisodio, NivelDolor, LocalizacionDolor,PromedioHorasSuenoSemanal,
    MedicamentoTomaActualmente,BebidasqueCausaronMigrana,AlimentosqueCausaronMigrana,ActividadFisicaqueCausaronMigrana   ) <>(Episodio.tupled, Episodio.unapply _)
}





