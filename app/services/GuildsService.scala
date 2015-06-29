package services

import javax.inject.{Singleton, Inject}

import domain._
import domain.aliases.GuildId
import play.api.Play
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class GuildsService @Inject() (ws: WSClient) {
  val remoteUrl = Play.current.configuration.getString("gw2.general.endpoints.guilds").get

  def nameById(id: GuildId): Future[Option[String]] = {
    val request = ws.url(remoteUrl).withQueryString("guild_id" -> id)

    request.get().map { response =>
      response.json.as[List[Guild]]
        .find { case Guild(gId, name, _, _) => gId == id }
        .map(_.guild_name)
    }
  }
}
