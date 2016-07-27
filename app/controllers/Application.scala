package controllers

import autenticacion.models.User
import autenticacion.{AnalistaTramitesAction, MedicoAction, PacienteAction, Secured}
import migrana.modelo.{Episodio, Paciente, Repository}
import migrana.services.migranaServices
import tramite.services.tramiteServices
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import pdi.jwt._
import play.twirl.api.Html
import usuariosApp.services.usuariosAppServices
import tramite.modelo._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class Application extends Controller with Secured {

  implicit val episodioFormat = Json.format[Episodio]
  implicit val pacienteFormat = Json.format[Paciente]

  implicit val atributoFormat = Json.format[atributo]
  implicit val campoFormat = Json.format[campo]
  implicit val PasoFormat = Json.format[Paso]
  implicit val TramiteFormat = Json.format[Tramite]

  val listaContraseñasPosibles = Seq("red", "blue", "green")  //lili

  def index = Action {
    Ok(views.html.index(new Html("")))
  }

  def consultaDePacientes = MedicoAction {
    Ok(views.html.consultaPacientes())
  }

  def consultaDeEpisodios = MedicoAction {
    Ok(views.html.consultarEpisodios())
  }

  def registrarEpisodio = PacienteAction {
    Ok(views.html.registrarEpisodio())
  }

  def agregarTramite = AnalistaTramitesAction {
    Ok(views.html.agregarUnTramite())
  }

  def generarHtmlTramite(ConfTramite:String) = Action { request =>
    Try(Json.parse(ConfTramite)) match {
      case Success(jsonTramite)=>{
        jsonTramite.validate[Tramite].asOpt match {
          case Some(tramite)=>
            val htmlTramite=tramiteServices.crearHtmlDelTramite(tramite)
            Ok(htmlTramite)
          case None=> Ok("El Json no corresponde a un tramite")
        }
      }
      case _ =>Ok("El Json tiene errores")
    }
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

  private val loginForm: Reads[(String, String)] =
    (JsPath \ "username").read[String] and
    (JsPath \ "password").read[String] tupled

  def login: Action[JsValue] = Action.async(parse.json) { implicit request =>{
    println("login")
    request.body.validate(loginForm).fold(
      errors => {
        Future(BadRequest(JsError.toJson(errors)))
      },
      form => {
        println("form ***")
        usuariosAppServices.getUsuario(form._1, form._2) map( usuario =>{
          println("usuario"+ usuario)
            usuario match {
              case Some(usu)=>
                Ok.addingToJwtSession("user", User(form._1,usu.role))
              case None=>Unauthorized
            }
        }
          )
      }
    )
    }
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
