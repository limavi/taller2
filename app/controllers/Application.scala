package controllers

import autenticacion.models.User
import autenticacion.Secured
import migrana.modelo.{Episodio, Paciente, Repository}
import migrana.services.migranaServices
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import pdi.jwt._
import play.twirl.api.Html
import views.html

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application extends Controller with Secured {

  implicit val episodioFormat = Json.format[Episodio]
  implicit val pacienteFormat = Json.format[Paciente]

  val listaContraseñasPosibles = Seq("red", "blue", "green")

  def index = Action {
    Ok(views.html.index(new Html("")))
  }
  //Admin
  def consultaDePacientes = Action {
    Ok(views.html.consultaPacientes())
  }

  def consultaDeEpisodios = Authenticated {
    Ok(views.html.consultarEpisodios())
  }

  def registrarEpisodio = Action {
    Ok(views.html.registrarEpisodio())
  }

  private val loginForm: Reads[(String, String)] =
    (JsPath \ "username").read[String] and
    (JsPath \ "password").read[String] tupled

  def login = Action(parse.json) { implicit request =>
    request.body.validate(loginForm).fold(
      errors => {
        BadRequest(JsError.toJson(errors))
      },
      form => {
        if (listaContraseñasPosibles.contains(form._2)) { //aqui nos debemos traer un rol
          Ok.addingToJwtSession("user", User(form._1,"Paciente"))
        } else {
          Unauthorized
        }
      }
    )
  }

  def consultarEpisodios(id: Option[ Long ]) = Action.async { implicit request =>
    val res =migranaServices.getEpisodios(id) map { episodio =>
      Ok( Json.toJson( episodio ) )
    }
    res.map( _.withHeaders( ( ACCESS_CONTROL_ALLOW_ORIGIN, "*" ), ( CONTENT_TYPE, "application/hal+json" ) ) )
  }

  def consultarEpisodiosPorPaciente(TipoDocumento: String, NumeroDocumento: Long)= Action.async { implicit request =>
    val res =migranaServices.getEpisodiosPorPaciente(TipoDocumento,NumeroDocumento) map { episodiosPorPaciente =>
      Ok( Json.toJson( episodiosPorPaciente ) )
    }
    res.map( _.withHeaders( ( ACCESS_CONTROL_ALLOW_ORIGIN, "*" ), ( CONTENT_TYPE, "application/hal+json" ) ) )
  }

  def consultarPacientes(TipoDocumento:Option[ String ],  NumeroDocumento: Option[Long ]): Action[AnyContent] = Action.async { implicit request =>
    val res =migranaServices.getPacientes(TipoDocumento,NumeroDocumento ) map { pacientes =>
      Ok( Json.toJson( pacientes ) )
    }
    res.map( _.withHeaders( ( ACCESS_CONTROL_ALLOW_ORIGIN, "*" ), ( CONTENT_TYPE, "application/hal+json" ) ) )
  }

  def agregarEpisodio= Action.async { implicit request =>
    request.body.asJson.map { json =>
      json.validate[Episodio].map{
        case (episodio) => {
          Repository.addEpisodio(episodio).map(resultado => {
            //migranaServices.generarAlertaPorMuchosDolores(episodio.IdPaciente)
            Ok("Episodio agregado satisfactoriamente")
          })
        }
      }.recoverTotal{
        e => Future(BadRequest("Existe un error en el JSON: "+ JsError.toFlatJson(e)))
      }
    }.getOrElse {
      Future(BadRequest("Se esperaba un json para ejecutar el POST"))
    }
  }

  def sincronizarEpisodios = Action { request =>
    request.body.asJson.map { json =>
      json.validate[List[Episodio]].map {
        case (listEpisodios) => {
          migranaServices.addListEpisodios(listEpisodios).map(resultado => println(resultado))
          Ok("Episodios sincronizados satisfactoriamente")
        }
      }.recoverTotal {
        e => BadRequest("Existe un error en el JSON: " + JsError.toFlatJson(e))
      }
    }.getOrElse {
      BadRequest("Se esperaba un json para ejecutar el POST")
    }
  }





}
