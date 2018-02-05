package domain.Slack

import play.api.libs.json.{Json, OFormat}

case class AuthTokenResponse(access_token: String)

object AuthTokenResponse {
  implicit val format: OFormat[AuthTokenResponse] = Json.format[AuthTokenResponse]
}
