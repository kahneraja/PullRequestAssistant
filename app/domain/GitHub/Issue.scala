package domain.GitHub

import play.api.libs.json.{Json, OFormat}

case class Issue(href: String)

object Issue {
  implicit val format: OFormat[Issue] = Json.format[Issue]
}
