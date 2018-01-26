package filters

import factories.EventFactory
import gateways.BaseSpec
import gateways.testDoubles.TimeProviderStub

class RecentReviewRequestEventFilterTests extends BaseSpec {

  "Event type is review_requested" should "be match" in {
    val event = EventFactory.build(event = "review_requested")
    val events = RecentReviewRequestEventFilter.filter(List(event), TimeProviderStub)
    events.size shouldBe 1
  }

  "Event type is anything" should "be not match" in {
    val event = EventFactory.build(event = "anything")
    val events = RecentReviewRequestEventFilter.filter(List(event), TimeProviderStub)
    events.size shouldBe 0
  }

}
