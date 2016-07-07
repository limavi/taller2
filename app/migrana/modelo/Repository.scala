package migrana.modelo

import migrana.modelo.PacienteTable
import org.joda.time.{DateTime, LocalDate}
import slick.lifted.TableQuery
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.{Json, Writes}

import scala.concurrent.{ExecutionContext, Future}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global

object Repository {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val episodioTableQuery = TableQuery[EpisodioTable]
  val pacienteQuery = TableQuery[PacienteTable]


  def addEpisodio(epi: Episodio): Future[String] = {
    dbConfig.db.run(episodioTableQuery += epi).map(res => "episodio agregado correctamente" ).recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def addListEpisodios(epi: List[Episodio]): Future[String] = {
    dbConfig.db.run(episodioTableQuery.++=(epi)).map(res => "episodios sincronizados  ").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def getEpisodios(id: Option[ Long] ): Future[Seq[Episodio]] = {
    val query: Query[EpisodioTable, Episodio, Seq] = id match {
      case Some(idPaciente) =>episodioTableQuery.filter(_.Id === id)
      case None=>episodioTableQuery
    }
    dbConfig.db.run(query.result)
  }

  def getEpisodiosPorPaciente(TipoDocumento: String, NumeroDocumento: Long ): Future[Seq[Episodio]] = {
    val query = for {
      (episodio, paciente) <- episodioTableQuery.join(pacienteQuery.filter(p => p.TipoDocumento === TipoDocumento && p.NumeroDocumento === NumeroDocumento)) on (_.IdPaciente === _.Id)
    }yield episodio
    dbConfig.db.run(query.result)
  }

  def getTotalEpisodosPorPaciente(idPaciente:  Long ): Future[Int] = {
    val query=episodioTableQuery.filter(_.IdPaciente === idPaciente).length
    dbConfig.db.run(query.result)
  }

  def getPacientes(TipoDocumento:Option[ String ],  NumeroDocumento: Option[Long ] ): Future[Seq[Paciente]] = {

    TipoDocumento match{
      case Some(tipo)=>  {
        NumeroDocumento match{
          case Some(num)=>{
            dbConfig.db.run(pacienteQuery.filter(p=>p.TipoDocumento === tipo && p.NumeroDocumento===num).result)
          }
          case None=>dbConfig.db.run(pacienteQuery.result)
        }
      }
      case None=>dbConfig.db.run(pacienteQuery.result)
    }
  }
}
