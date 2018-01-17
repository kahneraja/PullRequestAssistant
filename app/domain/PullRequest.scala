package domain

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

import play.api.libs.json.{Json, OFormat}

import gateways.TimeProvider

case class PullRequest(
  id: Int,
  title: String,
  html_url: String,
  created_at: LocalDateTime,
  updated_at: LocalDateTime,
  user: GitHubMember,
  requested_reviewers: List[GitHubMember]
) {

  def getHoursSinceUpdated(timeProvider: TimeProvider): Int = {
    updated_at.until(timeProvider.now(), ChronoUnit.HOURS).toInt
  }

}

object PullRequest {
  implicit val format: OFormat[PullRequest] = Json.format[PullRequest]
}
