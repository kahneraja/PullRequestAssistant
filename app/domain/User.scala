package domain

import play.api.libs.json.{Json, OFormat}

case class User(_id: String, gitHubToken: String, slackToken: String)

object User {
  implicit val format: OFormat[User] = Json.format[User]
}
