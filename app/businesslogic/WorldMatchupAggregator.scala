package businesslogic

import javax.inject.Inject

import domain._
import domain.aggregates.Continent
import domain.aliases.MatchId
import services.WorldService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class WorldMatchupAggregator @Inject() (worldService: WorldService) {

  def produceFrom(matches: Seq[Match]): Future[Map[String, WorldMatchup]] = {
    val grouped: Seq[Future[Map[String, WorldMatchup]]] = matches.map { m =>
      for (
        redName   <- worldService.nameById(m.red_world_id);
        greenName <- worldService.nameById(m.green_world_id);
        blueName  <- worldService.nameById(m.blue_world_id)
      ) yield {
        Map(
          m.red_world_id.toString -> WorldMatchup(
            redName.get, m.wvw_match_id,
            TeamColor.RED,
            continentFromId(m.wvw_match_id),
            Map(TeamColor.GREEN -> greenName.get, TeamColor.BLUE -> blueName.get)
          ),
          m.green_world_id.toString -> WorldMatchup(
            greenName.get,
            m.wvw_match_id,
            TeamColor.GREEN,
            continentFromId(m.wvw_match_id),
            Map(TeamColor.RED -> redName.get, TeamColor.BLUE -> blueName.get)
          ),
          m.blue_world_id.toString -> WorldMatchup(
            blueName.get,
            m.wvw_match_id,
            TeamColor.BLUE,
            continentFromId(m.wvw_match_id),
            Map(TeamColor.RED -> redName.get, TeamColor.GREEN -> greenName.get)
          )
        )
      }

    }
    // transform to one Future[Seq[Map]] and then merge all maps
    Future.sequence(grouped).map(_.reduce(_ ++ _))
  }

  def continentFromId(matchId: MatchId): Continent =
    if (matchId.startsWith("1")) Continent.AMERICA else Continent.EUROPE

}
