package migrana.modelo

import org.joda.time.{DateTime, LocalDate}
import slick.lifted.TableQuery
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.{Json, Writes}

import scala.concurrent.{ExecutionContext, Future}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import usuariosApp.modelo.{UsuarioApp, UsuarioAppTable}

object RepositoryUsuarioApp {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val UsuarioAppTableQuery = TableQuery[UsuarioAppTable]


  def consultarUsuario(usr:String, password:String): Future[Option[UsuarioApp]] = {
    println(usr + " -  " + password)
    val query: Query[UsuarioAppTable, UsuarioApp, Seq] =UsuarioAppTableQuery.filter(us=> us.username === usr && us.password===password)
    dbConfig.db.run(query.result.headOption)
  }
}
