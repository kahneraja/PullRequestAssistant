package filters

import domain.GitHub.Event
import gateways.TimeProvider

object RecentlyCreatedEventFilter {
  def filter(events: List[Event], timeProvider: TimeProvider): List[Event] = {
    events.filter { event =>
      val hoursSinceCreated = event.getHoursSinceCreated(timeProvider)
      hoursSinceCreated == 0
    }
  }
}
