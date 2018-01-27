package domain.GitHub

import play.api.libs.json.{Json, OFormat}

case class Links(issue: Issue)

object Links {
  implicit val format: OFormat[Links] = Json.format[Links]
}