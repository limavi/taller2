package migrana.services

import migrana.modelo._

import scala.concurrent.{ExecutionContext, Future}

object migranaServices {

  def addEpisodio(epiCompleto: Episodio): Future[String] = {
    Repository.addEpisodio(epiCompleto)
  }

  def addListEpisodios(epiCompleto: List[ Episodio]): Future[String] = {
    Repository.addListEpisodios(epiCompleto)
  }

  def getEpisodios(id: Option[ Long ]): Future[Seq[Episodio]] = {
    Repository.getEpisodios(id)
  }


  def getEpisodiosPorPaciente(TipoDocumento: String, NumeroDocumento: Long) = {
    Repository.getEpisodiosPorPaciente(TipoDocumento,NumeroDocumento)
  }

  def getPacientes(TipoDocumento:Option[ String ],  NumeroDocumento: Option[Long ]): Future[Seq[Paciente]] = {
    Repository.getPacientes(TipoDocumento, NumeroDocumento)
  }

  def generarAlertaPorMuchosDolores(idPaciente: Long ): Future[Int] = {
    import scala.concurrent.ExecutionContext.Implicits.global
    Repository.getTotalEpisodosPorPaciente(idPaciente).map(total=> {
      if(total>12983){
        println("Alerta!!! usted ya ha tenido mas de 12983 episodios de migrana, por favor saque una cita " )}
      total
    })
  }
}
