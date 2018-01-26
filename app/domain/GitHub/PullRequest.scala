package domain.GitHub

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

import gateways.TimeProvider
import play.api.libs.json.{Json, OFormat}

case class PullRequest(
  title: String,
  html_url: String,
  created_at: LocalDateTime,
  updated_at: LocalDateTime,
  user: Member,
  requested_reviewers: List[Member]
) {

  def getHoursSinceUpdated(timeProvider: TimeProvider): Int = {
    updated_at.until(timeProvider.now(), ChronoUnit.HOURS).toInt
  }

}

object PullRequest {
  implicit val format: OFormat[PullRequest] = Json.format[PullRequest]
}
