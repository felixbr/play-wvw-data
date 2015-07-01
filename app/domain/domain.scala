package domain

import domain.aliases._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import com.wordnik.swagger.annotations._

import scala.annotation.meta.field

package object aliases {
  type MatchId = String
  type ObjectiveId = Int
  type GuildId = String
  type TeamColor = String
  type WorldName = String
}

object TeamColor {
  val RED = "Red"
  val GREEN = "Green"
  val BLUE = "Blue"
  val WHITE = "White"
}

case class Match(
  wvw_match_id: MatchId,
  red_world_id: Int,
  blue_world_id: Int,
  green_world_id: Int,
  start_time: String,
  end_time: String
)

case class MatchDetails(
                                               match_id: String,
  @(ApiModelProperty @field)(dataType = "int") scores: Seq[Int],
                                               maps: Seq[MapDetails]
)

case class MapDetails(
                                               `type`: String,
  @(ApiModelProperty @field)(dataType = "int") scores: Seq[Int],
                                               objectives: Seq[Objective]
)

case class Objective(
  id: ObjectiveId,
  owner: String,
  owner_guild: Option[GuildId]
)

case class ObjectiveMapping(
  id: ObjectiveId,
  name: String
)

case class Guild(
  guild_id: GuildId,
  guild_name: String,
  tag: String,
  emblem: Emblem
)

case class Emblem(
  background_id: Int,
  foreground_id: Int,
  flags: Seq[Int],
  background_color_id: Int,
  foreground_primary_color_id: Int,
  foreground_secondary_color_id: Int
)

case class World(
  id: String,
  name: WorldName
)

object Match {
  implicit val matchFormat = Json.format[Match]
}

object MatchDetails {
  implicit val matchDetailsFormat = Json.format[MatchDetails]
}

object Objective {
  implicit val objectiveFormat = Json.format[Objective]
}

object ObjectiveMapping {
  implicit val objectiveMappingFormat = Json.format[ObjectiveMapping]
}

object Emblem {
  implicit val emblemFormat = Json.format[Emblem]
}

object Guild {
  implicit val guildFormat = Json.format[Guild]
}

object World {
  implicit val worldFormat = Json.format[World]
}

//noinspection ConvertibleToMethodValue
object MapDetails {
  implicit val mapDetailsFormat: Format[MapDetails] = (
  (__ \ "type").format[String] and
  (__ \ "scores").format[Seq[Int]] and
  (__ \ "objectives").format[Seq[Objective]]
  )(MapDetails.apply _, unlift(MapDetails.unapply _))
}

