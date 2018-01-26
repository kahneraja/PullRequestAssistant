package gateways

import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.time.{Millis, Minutes, Span}
import org.scalatest.{FlatSpec, Matchers}

class BaseSpec extends FlatSpec
  with Matchers
  with ScalaFutures
  with Eventually
  with MockitoSugar {
  implicit override val patienceConfig = PatienceConfig(
    timeout = Span(1, Minutes),
    interval = Span(500, Millis)
  )
}
