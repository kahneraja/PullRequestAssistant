package domain.GitHub

import java.time.LocalDateTime

import play.api.libs.json.{Json, OFormat}

case class Event (
  event: String,
  actor: Member,
  review_requester: Option[Member],
  requested_reviewer: Option[Member],
  requested_team: Option[Team],
  created_at: LocalDateTime
)

object Event {
  implicit val format: OFormat[Event] = Json.format[Event]
}

