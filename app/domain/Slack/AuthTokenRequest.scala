package domain.Slack

import play.api.libs.json.{Json, OFormat}

case class AuthTokenRequest(
  client_id: String,
  client_secret: String,
  code: String,
  userId: String,
  redirect_uri: String
)

object AuthTokenRequest {
  implicit val format: OFormat[AuthTokenRequest] = Json.format[AuthTokenRequest]
}
