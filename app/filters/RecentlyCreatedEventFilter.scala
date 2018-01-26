package filters

import java.time.temporal.ChronoUnit

import domain.GitHub.Event
import gateways.TimeProvider

class RecentlyCreatedEventFilter(timeProvider: TimeProvider) {
  def filter(events: List[Event]): List[Event] = {
    events.filter { event =>
      val hoursSinceCreated = event.getHoursSinceCreated(timeProvider)
      hoursSinceCreated == 0
    }
  }
}
