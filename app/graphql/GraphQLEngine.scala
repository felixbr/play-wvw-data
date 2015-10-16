package graphql

import javax.inject.{Inject, Singleton}

import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import sangria.ast.Document
import sangria.execution.Executor
import sangria.parser.QueryParser
import sangria.introspection.introspectionQuery

import scala.concurrent.Future
import scala.util.{Failure, Success}

case class QueryError(msg: JsValue) extends Exception(msg.toString())

@Singleton
class GraphQLEngine @Inject() (repo: WvWRepo) {
  import sangria.integration.playJson._

  private val executor = new Executor(
    schema = schema.schema,
    userContext = repo
  )

  def process(query: String): Future[JsValue] = {
    parseQuery(query) match {
      case Success(queryAST: Document) =>
        for {
          result <- executor.execute(queryAST)
          errors = (result \ "errors").toOption
        } yield errors match {
          case Some(_) => throw QueryError(result)
          case None    => (result \ "data").get
        }

      case Failure(exc) =>
        Future.failed(exc)
    }
  }

  def schemaAsJson(): Future[JsValue] = {
    executor.execute(introspectionQuery)
  }

  private def parseQuery(query: String) = {
    QueryParser.parse(query)
  }

}
