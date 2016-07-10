package usuariosApp.services

import migrana.modelo.RepositoryUsuarioApp
import usuariosApp.modelo._

import scala.concurrent.{ExecutionContext, Future}

object usuariosAppServices {

  def getUsuario(user:String, password:String): Future[Option[UsuarioApp]] = {
    RepositoryUsuarioApp.consultarUsuario(user,password )
  }

}
