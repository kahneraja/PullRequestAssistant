package domain

import play.api.libs.json.{Json, OFormat}

case class GitHubMember(id: Int, login: String, url: String)

object GitHubMember {
  implicit val format: OFormat[GitHubMember] = Json.format[GitHubMember]
}
