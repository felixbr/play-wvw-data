package controllers

import javax.inject.Inject

import play.api.libs.json._
import play.api.mvc._
import services.{MatchDetailsService, MatchesService}

import scala.concurrent.ExecutionContext.Implicits.global

class MatchesController @Inject() (
  matches: MatchesService,
  matchDetails: MatchDetailsService) extends Controller {

  def list = Action.async {
    matches.fetch().map { list =>
      Ok(Json.toJson(list))
    }
  }

  def details(id: String) = Action.async {
    matchDetails.fetchById(id).map { details =>
      Ok(Json.toJson(details))
    }
  }

}
