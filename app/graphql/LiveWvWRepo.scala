package graphql

import javax.inject.{Singleton, Inject}

import domain.MatchDetails
import services.MatchDetailsService

import scala.concurrent.Future

@Singleton
class LiveWvWRepo @Inject() (
  matchDetailsService: MatchDetailsService
) extends WvWRepo {

  override def fetchMatchDetails(id: String): Future[MatchDetails] = {
    matchDetailsService.fetchById(id)
  }
}
