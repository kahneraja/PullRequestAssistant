package gateways

import java.time._

class TimeProviderImpl extends TimeProvider {
  override def now(): LocalDateTime = LocalDateTime.now(Clock.systemUTC())
  override def est(): LocalDateTime = {
    val estOffset = 5
    now().minusHours(estOffset)
  }
}
