package utils

import play.api.libs.json.Json

case class ErrorResponse(error: String)

object ErrorResponse {
  implicit val errorResponseFormat = Json.format[ErrorResponse]
}
