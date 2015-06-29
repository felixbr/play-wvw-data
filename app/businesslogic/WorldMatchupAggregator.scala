package businesslogic

import javax.inject.Inject

import domain._
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
            Map(TeamColor.GREEN -> greenName.get, TeamColor.BLUE -> blueName.get)
          ),
          m.green_world_id.toString -> WorldMatchup(
            greenName.get,
            m.wvw_match_id,
            TeamColor.GREEN,
            Map(TeamColor.RED -> redName.get, TeamColor.BLUE -> blueName.get)
          ),
          m.blue_world_id.toString -> WorldMatchup(
            blueName.get,
            m.wvw_match_id,
            TeamColor.BLUE,
            Map(TeamColor.RED -> redName.get, TeamColor.GREEN -> greenName.get)
          )
        )
      }

    }
    // transform to one Future[Seq[Map]] and then merge all maps
    Future.sequence(grouped).map(_.reduce(_ ++ _))
  }

}
