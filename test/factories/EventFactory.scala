package factories

import java.time.{Clock, LocalDateTime}

import domain.GitHub.{Event, Member, Team}
import gateways.testDoubles.TimeProviderStub

object EventFactory {
  def build(
    event: String = "",
    actor: Member = MemberFactory.build(),
    review_requester: Option[Member] = Some(MemberFactory.build()),
    requested_reviewer: Option[Member] = Some(MemberFactory.build()),
    requested_team: Option[Team] = Some(TeamFactory.build()),
    created_at: LocalDateTime = TimeProviderStub.now()
  ): Event = {
    new Event(
      event = event,
      actor = actor,
      requested_reviewer = requested_reviewer,
      review_requester = review_requester,
      requested_team = requested_team,
      created_at = created_at
    )
  }
}
