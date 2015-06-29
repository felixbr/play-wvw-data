package services

import javax.inject.{Singleton, Inject}

import domain._
import play.api._
import play.api.libs.ws._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class MatchDetailsService @Inject() (ws: WSClient) {
  val remoteUrl = Play.current.configuration.getString("gw2.wvw.endpoints.match_details").get

  def fetchById(matchId: String) = {
    val request = ws.url(remoteUrl).withQueryString("match_id" -> matchId)

    request.get().map { response =>
      response.json.as[MatchDetails]
    }
  }
}
