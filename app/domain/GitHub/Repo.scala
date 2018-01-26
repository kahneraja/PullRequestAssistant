package domain.GitHub

import play.api.libs.json.{Json, OFormat}

case class Repo(url: String)
object Repo {
  implicit val format: OFormat[Repo] = Json.format[Repo]
}
