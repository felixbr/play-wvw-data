package domain

import domain.aliases._
import play.api.libs.json.Json

package object aggregates {

}

case class WorldMatchup(name: WorldName, wvw_match_id: MatchId, color: TeamColor, plays_against: Map[TeamColor, WorldName])

object WorldMatchup {
  implicit val worldMatchupFormat = Json.format[WorldMatchup]
}