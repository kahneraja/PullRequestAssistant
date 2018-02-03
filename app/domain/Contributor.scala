package domain

import play.api.libs.json.{Json, OFormat}

case class Contributor(github_name: String, slack_name: String)
object Contributor {
  implicit val format: OFormat[Contributor] = Json.format[Contributor]
}
