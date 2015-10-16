package utils

import play.api.libs.json.{JsValue, Json}

case class ErrorResponse(error: String)

object ErrorResponse {
  implicit val errorResponseFormat = Json.format[ErrorResponse]

  def asJson(exc: Exception): JsValue =
    Json.toJson(ErrorResponse(exc.getMessage))
}
