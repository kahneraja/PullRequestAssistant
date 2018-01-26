package domain.GitHub

import play.api.libs.json.{Json, OFormat}

case class Team(name: String, url: String)

object Team {
  implicit val format: OFormat[Team] = Json.format[Team]
}
