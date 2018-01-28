package domain.GitHub

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

import gateways.TimeProvider
import play.api.libs.json.{Json, OFormat}

case class PullRequest(
  title: String,
  url: String,
  html_url: String,
  created_at: LocalDateTime,
  updated_at: LocalDateTime,
  closed_at: Option[LocalDateTime],
  user: Member,
  requested_reviewers: List[Member],
  requested_teams: List[Team],
  _links: Links,
  files: Option[List[File]]
) {
  def getHoursSinceUpdated(timeProvider: TimeProvider): Int = {
    updated_at.until(timeProvider.now(), ChronoUnit.HOURS).toInt
  }

}

object PullRequest {
  implicit val format: OFormat[PullRequest] = Json.format[PullRequest]
}

