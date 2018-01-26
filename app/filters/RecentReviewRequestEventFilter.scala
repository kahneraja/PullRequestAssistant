package filters

import domain.GitHub.Event
import gateways.TimeProvider

class RecentReviewRequestEventFilter(timeProvider: TimeProvider) {
  def filter(events: List[Event]): List[Event] = {
    new RecentlyCreatedEventFilter(timeProvider).filter(events).filter { event =>
      event.event == "review_requested"
    }
  }
}
