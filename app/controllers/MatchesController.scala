package controllers

import javax.inject.Inject

import domain._
import com.wordnik.swagger.annotations._
import play.api.libs.json._
import play.api.mvc._
import services.{MatchDetailsService, MatchesService}
import utils.ErrorResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

@Api(value = "/matches", description = "Routes for matches")
class MatchesController @Inject() (
  matches: MatchesService,
  matchDetails: MatchDetailsService) extends Controller {

  @ApiOperation(
    value = "List of matches",
    httpMethod = "GET",
    response = classOf[Match]
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK")
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
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 404, message = "Not Found")
  ))
  def details(id: String) = Action.async {
    matchDetails.fetchById(id).map { details =>
      Ok(Json.toJson(details))
    } recover {
      case _ => NotFound(Json.toJson(ErrorResponse(s"Couldn't find match for match_id $id")))
    }
  }

}
