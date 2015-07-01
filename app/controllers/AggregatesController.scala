package controllers

import javax.inject.Inject

import businesslogic.WorldMatchupAggregator
import com.wordnik.swagger.annotations._
import domain.WorldMatchup
import play.api.libs.json.Json
import play.api.mvc._
import services.MatchesService

import scala.concurrent.ExecutionContext.Implicits.global

@Api(value = "/aggregates", description = "Routes for aggregates")
class AggregatesController @Inject() (
  matchesService: MatchesService,
  worldAggregator: WorldMatchupAggregator) extends Controller {

  @ApiOperation(
    value = "List of Realms",
    httpMethod = "GET",
    response = classOf[WorldMatchup]
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK")
  ))
  def realms = Action.async {
    for (
      matches    <- matchesService.fetch();
      aggregates <- worldAggregator.produceFrom(matches)
    ) yield Ok(Json.toJson(aggregates))
  }

}