package domain

import play.api.libs.json.{Json, OFormat}

case class User(github_name: String, slack_name: String)
object User {
  implicit val format: OFormat[User] = Json.format[User]
}
