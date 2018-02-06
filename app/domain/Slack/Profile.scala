package domain.Slack

import play.api.libs.json.{Json, OFormat}

case class Profile(real_name: String)

object Profile {
  implicit val format: OFormat[Profile] = Json.format[Profile]
}
