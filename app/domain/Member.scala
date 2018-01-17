package domain

import play.api.libs.json.{Json, OFormat}

case class Member(github_name: String, slack_name: String)
object Member {
  implicit val format: OFormat[Member] = Json.format[Member]
}
