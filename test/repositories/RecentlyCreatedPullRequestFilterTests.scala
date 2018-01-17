package repositories

import factories.PullRequestFactory
import gateways.testDoubles.TimeProviderStub
import org.scalatest._

class RecentlyCreatedPullRequestFilterTests extends FlatSpec with Matchers {

  "Created 30 minutes ago" should "be recent" in {
    val minutes = 30
    val pullRequest = PullRequestFactory.build(created_at = TimeProviderStub.now().minusMinutes(minutes))
    val pullRequests = new RecentlyCreatedPullRequestFilterImpl(TimeProviderStub).filter(List(pullRequest))
    pullRequests.size shouldBe 1
  }

  "Created 90 minutes ago" should "NOT be recent" in {
    val minutes = 90
    val pullRequest = PullRequestFactory.build(created_at = TimeProviderStub.now().minusMinutes(minutes))
    val pullRequests = new RecentlyCreatedPullRequestFilterImpl(TimeProviderStub).filter(List(pullRequest))
    pullRequests.size shouldBe 0
  }

}
