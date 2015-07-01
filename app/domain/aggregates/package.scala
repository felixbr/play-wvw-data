package domain

import domain.aggregates.Continent
import domain.aliases._
import play.api.libs.json.Json

package object aggregates {
  type Continent = String
}

object Continent {
  val AMERICA = "America"
  val EUROPE = "Europe"
}

case class WorldMatchup(
  name: WorldName,
  wvw_match_id: MatchId,
  color: TeamColor,
  continent: Continent,
  plays_against: Map[TeamColor, WorldName]
)

object WorldMatchup {
  implicit val worldMatchupFormat = Json.format[WorldMatchup]
}