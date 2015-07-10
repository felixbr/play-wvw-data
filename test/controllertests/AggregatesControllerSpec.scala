package controllertests

import play.api.libs.json._
import play.api.test._
import domain._

class AggregatesControllerSpec extends PlaySpecification {

  "The AggregateController" should {

    "respond with json list" in new WithServer {
      val result = route(FakeRequest(GET, "/aggregates/realms")).get

      status(result) must equalTo(OK)
      contentType(result) must equalTo(Some("application/json"))

      val json = contentAsJson(result)
      json must beAnInstanceOf[JsObject]
      json.as[Map[String, WorldMatchup]].values.size must beGreaterThan(0)
    }

  }

}
