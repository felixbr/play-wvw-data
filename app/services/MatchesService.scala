package services

import javax.inject.{Singleton, Inject}

import domain._
import play.api._
import play.api.libs.ws._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class MatchesService @Inject() (ws: WSClient) {
  val remoteUrl = Play.current.configuration.getString("gw2.wvw.endpoints.matches").get

  def fetch(): Future[List[Match]] = {
    val request = ws.url(remoteUrl)

    request.get().map { response =>
      (response.json \ "wvw_matches").as[List[Match]]
    }
  }
}
