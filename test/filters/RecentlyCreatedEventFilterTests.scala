package filters

import factories.EventFactory
import gateways.BaseSpec
import gateways.testDoubles.TimeProviderStub

class RecentlyCreatedEventFilterTests extends BaseSpec {

  "Created in last 30 minutes" should "be recent" in {
    val minutes = 30
    val event = EventFactory.build(created_at = TimeProviderStub.now().minusMinutes(minutes))
    val events = RecentlyCreatedEventFilter.filter(List(event), TimeProviderStub)
    events.size shouldBe 1
  }

  "Created in last 90 minutes" should "be recent" in {
    val minutes = 90
    val event = EventFactory.build(created_at = TimeProviderStub.now().minusMinutes(minutes))
    val events = RecentlyCreatedEventFilter.filter(List(event), TimeProviderStub)
    events.size shouldBe 0
  }

}
