package tramite.services

import migrana.modelo._
import tramite.modelo.Tramite

object tramiteServices {

  def crearHtmlDelTramite(confTramite: Tramite): String = {
    val nombreTramite= confTramite.Nombre
    val descripcion= confTramite.Descripcion

    val Encabezado = s"<center><h1>$nombreTramite</h1><br><h4>$descripcion</h4><center>"
    val body =""

    println(Encabezado + body)
     Encabezado + body
  }

}
