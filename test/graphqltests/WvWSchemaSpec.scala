package graphqltests

import domain._
import graphql._
import graphql.schema.schema
import graphqltests.WvWSchemaSpec._
import org.scalatestplus.play._
import play.api.test._
import sangria.ast.Document
import sangria.execution.Executor
import sangria.macros._
import services.MatchDetailsService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object WvWSchemaSpec {
  object MockWvWRepo extends WvWRepo {
    override def fetchMatchDetails(id: String): Future[MatchDetails] = id match {
      case "2-2" =>
        Future.successful(MatchDetails(
          "2-2",
          List(9001, 9001, 9001),
          List.empty
        ))

      case _ =>
        Future.failed(new Exception("invalid id"))
    }
  }

}

//noinspection EmptyParenMethodAccessedAsParameterless
class WvWSchemaSpec extends PlaySpec with OneAppPerSuite {
  import sangria.integration.playJson._

  val query: Document =
    graphql"""
      {
        match_details(id: "2-2") {
          match_id,
          scores,
          maps {
            type,
            scores,
            objectives {
              id,
              owner,
              owner_guild
            }
          }
        }
      }
    """

  "The graphql schema" should {
    "process a simple query for mock MatchDetails" in {
      val mockExecutor = Executor(schema = schema, userContext = MockWvWRepo)

      val queryResult = Await.result(mockExecutor.execute(query), 3.seconds)

      val matchDetails = (queryResult \ "data" \ "match_details").as[MatchDetails]
      matchDetails.scores.size must be (3)
    }

    "process a query for live MatchDetails" in {
      WsTestClient.withClient { ws =>
        val liveWvWRepo = new LiveWvWRepo(new MatchDetailsService(ws))
        val liveExecutor = Executor(schema = schema, userContext = liveWvWRepo)

        val queryResult = Await.result(liveExecutor.execute(query), 3.seconds)

        val matchDetails = (queryResult \ "data" \ "match_details").as[MatchDetails]
        matchDetails.scores.size must be (3)
      }
    }
  }

}
