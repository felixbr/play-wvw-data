package services

import javax.inject.{Inject, Singleton}

import domain.{World, Guild}
import play.api.Play
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class WorldService @Inject() (ws: WSClient) {
  val remoteUrl = Play.current.configuration.getString("gw2.general.endpoints.worlds").get

  def nameById(id: Int): Future[Option[String]] = {
    val request = ws.url(remoteUrl)

    request.get().map { response =>
      response.json.as[List[World]]
        .find { case World(wId, _) => wId == id.toString }
        .map(_.name)
    }
  }
}
