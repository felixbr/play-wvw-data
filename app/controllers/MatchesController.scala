package controllers

import java.lang.annotation.Annotation
import javax.inject.Inject

import domain._
import com.wordnik.swagger.annotations._
import play.api.libs.json._
import play.api.mvc._
import services.{MatchDetailsService, MatchesService}

import scala.concurrent.ExecutionContext.Implicits.global

@Api("/matches")
class MatchesController @Inject() (
  matches: MatchesService,
  matchDetails: MatchDetailsService) extends Controller {

  @ApiOperation(
    value = "List of matches",
    httpMethod = "GET"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "List of matches")
  ))
  def list = Action.async {
    matches.fetch().map { list =>
      Ok(Json.toJson(list))
    }
  }

  @ApiOperation(
    value = "MatchDetails by id",
    httpMethod = "GET",
    response = classOf[MatchDetails]
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", value = "Match id", required = true, defaultValue = "2-2", paramType = "path")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "MatchDetails by id")
  ))
  def details(id: String) = Action.async {
    matchDetails.fetchById(id).map { details =>
      Ok(Json.toJson(details))
    }
  }

}
