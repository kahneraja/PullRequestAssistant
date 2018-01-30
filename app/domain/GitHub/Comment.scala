package domain.GitHub

import play.api.libs.json.{Json, OFormat}

case class Comment(url: String, body: String  )

object Comment {
  implicit val format: OFormat[Comment] = Json.format[Comment]
}
