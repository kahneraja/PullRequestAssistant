package domain.GitHub

import play.api.libs.json.{Json, OFormat}

case class Links(issue: Href, comments: Href)

object Links {
  implicit val format: OFormat[Links] = Json.format[Links]
}
