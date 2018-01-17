package gateways.testDoubles

import java.time.LocalDateTime

import gateways.TimeProvider

object TimeProviderStub extends TimeProvider {
  override def now(): LocalDateTime = LocalDateTime.of(2018, 1, 1, 0, 0)
  override def est(): LocalDateTime = ???
}
