package tramite.services

import migrana.modelo._
import tramite.modelo.Tramite

object tramiteServices {

  def crearHtmlDelTramite(confTramite: Tramite): String = {
    val nombreTramite= confTramite.Nombre
    val descripcion= confTramite.Descripcion

    val Encabezado = s"<center><h4>$nombreTramite</h4><h6>$descripcion</h6><center>"
    val body =""

    println(Encabezado + body)
     Encabezado + body
  }

}
