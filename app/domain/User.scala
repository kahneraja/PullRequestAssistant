package domain

import play.api.libs.json.{Json, OFormat}

case class User(_id: String, githubToken: String)

object User {
  implicit val format: OFormat[User] = Json.format[User]
}
