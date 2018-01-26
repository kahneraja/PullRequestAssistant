package filters

import domain.GitHub.Event
import gateways.TimeProvider

object RecentReviewRequestEventFilter {
  def filter(events: List[Event], timeProvider: TimeProvider): List[Event] = {
    RecentlyCreatedEventFilter.filter(events, timeProvider).filter { event =>
      event.event == "review_requested"
    }
  }
}
