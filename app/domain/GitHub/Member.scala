package domain.GitHub

import play.api.libs.json.{Json, OFormat}

case class Member(login: String, url: String)

object Member {
  implicit val format: OFormat[Member] = Json.format[Member]
}
