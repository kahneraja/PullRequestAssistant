package repositories

import factories.PullRequestFactory
import gateways.testDoubles.TimeProviderStub
import org.scalatest._

class IdlePullRequestFilterTests extends FlatSpec with Matchers {

  "Update 24 hours 30 minutes ago" should "be idle" in {
    val pullRequest = PullRequestFactory.build(updated_at = TimeProviderStub.now().minusHours(24).minusMinutes(20))
    val pullRequests = new IdlePullRequestFilterImpl(TimeProviderStub).filter(List(pullRequest))
    pullRequests.size shouldBe 1
  }

  "1 hour ago" should "not be idle" in {
    val pullRequest = PullRequestFactory.build(updated_at = TimeProviderStub.now().minusHours(1))
    val pullRequests = new IdlePullRequestFilterImpl(TimeProviderStub).filter(List(pullRequest))
    pullRequests.size shouldBe 0
  }

}
