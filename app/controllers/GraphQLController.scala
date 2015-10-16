package controllers

import javax.inject.{Inject, Singleton}

import graphql.{QueryError, GraphQLEngine}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._
import sangria.parser.SyntaxError
import sangria.renderer.SchemaRenderer
import utils.ErrorResponse

@Singleton
class GraphQLController @Inject() (graphQLEngine: GraphQLEngine) extends Controller {
  import sangria.integration.playJson._

  def index = Action.async {
    graphQLEngine.schemaAsJson().map { res =>
      SchemaRenderer
        .renderSchema(res)
        .map(Ok(_))
        .getOrElse(InternalServerError("Can't render the schema!"))
    }
  }

  def query = Action.async(parse.text) { req =>
    graphQLEngine.process(req.body).map { res =>
      Logger.info(res.toString())
      Ok(res)
    }.recover {
      case QueryError(json) => BadRequest(json)
      case e: SyntaxError => BadRequest(ErrorResponse.asJson(e))
      case e: Exception => InternalServerError(ErrorResponse.asJson(e))
    }
  }

}
