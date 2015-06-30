package controllers

import javax.inject.Inject

import businesslogic.WorldMatchupAggregator
import com.wordnik.swagger.annotations._
import play.api.libs.json.Json
import play.api.mvc._
import services.MatchesService

import scala.concurrent.ExecutionContext.Implicits.global

@Api(value = "/aggregates", description = "Route for aggregates")
class AggregatesController @Inject() (
  matchesService: MatchesService,
  worldAggregator: WorldMatchupAggregator) extends Controller {

  @ApiOperation(
    value = "List of Realms",
    httpMethod = "GET"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "List of realms aggregated")
  ))
  def realms = Action.async {
    for (
      matches    <- matchesService.fetch();
      aggregates <- worldAggregator.produceFrom(matches)
    ) yield Ok(Json.toJson(aggregates))
  }

}