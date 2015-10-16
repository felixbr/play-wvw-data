package graphql

import com.google.inject.ImplementedBy
import domain._
import sangria.schema._

import scala.concurrent.Future

@ImplementedBy(classOf[LiveWvWRepo])
trait WvWRepo {
  def fetchMatchDetails(id: String): Future[MatchDetails]
}

object schema {
  private val ObjectiveType = ObjectType("Objective", fields[Unit, Objective](
    Field("id", IntType, resolve = _.value.id),
    Field("owner", StringType, resolve = _.value.owner),
    Field("owner_guild", OptionType(StringType), resolve = _.value.owner_guild)
  ))

  private val MapType = ObjectType("MapDetails", fields[Unit, MapDetails](
    Field("type", StringType, resolve = _.value.`type`),
    Field("scores", ListType(IntType), resolve = _.value.scores),
    Field("objectives", ListType(ObjectiveType), resolve = _.value.objectives)
  ))

  private val MatchDetailsType = ObjectType("MatchDetails", fields[Unit, MatchDetails](
    Field("match_id", StringType, resolve = _.value.match_id),
    Field("scores", ListType(IntType), resolve = _.value.scores),
    Field("maps", ListType(MapType), resolve = _.value.maps)
  ))

  val schema = Schema(ObjectType("Query", fields[WvWRepo, Unit](
    Field("match_details", OptionType(MatchDetailsType),
      arguments = Argument("id", OptionInputType(IDType)) :: Nil,
      resolve = ctx => ctx.ctx.fetchMatchDetails(ctx.arg("id"))
  ))))
}

