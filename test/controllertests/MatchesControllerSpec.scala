package controllertests

import play.api.libs.json._
import play.api.test._
import domain._

class MatchesControllerSpec extends PlaySpecification {

  "The MatchesController" should {

    "respond with json list of all matches" in new WithServer {
      val result = route(FakeRequest(GET, "/matches")).get

      status(result) must equalTo(OK)
      contentType(result) must equalTo(Some("application/json"))

      val json = contentAsJson(result)
      json must beAnInstanceOf[JsArray]
      json.as[List[Match]].size must beGreaterThan(0)
    }

    "respond with details of a match for matchId" in new WithServer {
      val id = "2-2"
      val result = route(FakeRequest(GET, s"/match-details/$id")).get

      status(result) must equalTo(OK)
      contentType(result) must equalTo(Some("application/json"))

      val json = contentAsJson(result)
      json must beAnInstanceOf[JsObject]
      json.as[MatchDetails]
    }

    "return 404 for invalid matchIds" in new WithServer {
      val badId = "abc"
      val result = route(FakeRequest(GET, s"/match-details/$badId")).get

      status(result) must equalTo(404)
      contentType(result) must equalTo(Some("application/json"))
    }

  }

}
