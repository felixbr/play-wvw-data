package controllers

import javax.inject.Inject

import businesslogic.WorldMatchupAggregator
import play.api.libs.json.Json
import play.api.mvc._
import services.MatchesService

import scala.concurrent.ExecutionContext.Implicits.global

class AggregatesController @Inject() (
  matchesService: MatchesService,
  worldAggregator: WorldMatchupAggregator) extends Controller {

  def realms = Action.async {
    for (
      matches    <- matchesService.fetch();
      aggregates <- worldAggregator.produceFrom(matches)
    ) yield Ok(Json.toJson(aggregates))
  }

}