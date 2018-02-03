package domain.GitHub

import play.api.libs.json.{Json, OFormat}

case class AuthResponse(id: String)

object AuthResponse {
  implicit val format: OFormat[AuthResponse] = Json.format[AuthResponse]
}



