package repositories

import factories.PullRequestFactory
import gateways.BaseSpec
import gateways.testDoubles.TimeProviderStub

class RecentlyCreatedPullRequestFilterTests extends BaseSpec {

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
