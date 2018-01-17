package gateways

import java.time.LocalDateTime

trait TimeProvider {
  def now(): LocalDateTime
  def est(): LocalDateTime
}
