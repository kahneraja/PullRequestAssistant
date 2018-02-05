package domain.Slack

import play.api.libs.json.{Json, OFormat}

case class AccessToken (
  access_token: String
)

object AccessToken {
  implicit val format: OFormat[AccessToken] = Json.format[AccessToken]
}
