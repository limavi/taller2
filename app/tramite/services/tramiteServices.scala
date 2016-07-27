package tramite.services

import migrana.modelo._
import tramite.modelo.Tramite

object tramiteServices {

  def crearHtmlDelTramite(confTramite: Tramite): String = {
    val NombreTramite= confTramite.Nombre

    val Encabezado = s"<html><head><title>$NombreTramite</title></head>"
    val body =""

     Encabezado + body
  }

}
