package services

import javax.inject.{Inject, Singleton}

import domain._
import domain.aliases.ObjectiveId
import play.api.Play
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ObjectiveService @Inject() (ws: WSClient) {
  val remoteUrl = Play.current.configuration.getString("gw2.www.endpoints.objective_names").get

  def nameById(id: ObjectiveId): Future[Option[String]] = {
    val request = ws.url(remoteUrl)

    request.get().map { response =>
      response.json.as[List[ObjectiveMapping]]
        .find { case ObjectiveMapping(objId, name) => objId == id }
        .map(_.name)
    }
  }
}
