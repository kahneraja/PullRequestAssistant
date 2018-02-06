package domain.Slack

import play.api.libs.json.{Json, OFormat}

case class Member(id: String, real_name: String, profile: Profile)

object Member {
  implicit val format: OFormat[Member] = Json.format[Member]
}
