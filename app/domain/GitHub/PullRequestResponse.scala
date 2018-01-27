package domain.GitHub

import java.time.LocalDate

import play.api.libs.json.{Json, OFormat}

case class PullRequestResponse (title: String, url: String, label: LocalDate, hours: Int) {
}

object PullRequestResponse {
  implicit val format: OFormat[PullRequestResponse] = Json.format[PullRequestResponse]
}
