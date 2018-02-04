package domain.GitHub

import play.api.libs.json.{Json, OFormat}

case class Org(id: Int, url: String, userId: String)

object Org {
  implicit val format: OFormat[Org] = Json.format[Org]
}


