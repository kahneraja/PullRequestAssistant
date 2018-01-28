package domain.GitHub

import play.api.libs.json.{Json, OFormat}

case class File(filename: String, changes: Int)

object File {
  implicit val format: OFormat[File] = Json.format[File]
}
