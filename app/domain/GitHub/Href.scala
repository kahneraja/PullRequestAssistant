package domain.GitHub

import play.api.libs.json.{Json, OFormat}

case class Href(href: String)

object Href {
  implicit val format: OFormat[Href] = Json.format[Href]
}
