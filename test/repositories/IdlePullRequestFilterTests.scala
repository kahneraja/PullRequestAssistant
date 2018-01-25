package repositories

import factories.PullRequestFactory
import gateways.BaseSpec
import gateways.testDoubles.TimeProviderStub

class IdlePullRequestFilterTests extends BaseSpec {

  "Update 24 hours 30 minutes ago" should "be idle" in {
    val hours = 24
    val minutes = 20
    val pullRequest = PullRequestFactory.build(updated_at = TimeProviderStub.now().minusHours(hours).minusMinutes(minutes))
    val pullRequests = new IdlePullRequestFilterImpl(TimeProviderStub).filter(List(pullRequest))
    pullRequests.size shouldBe 1
  }

  "Update 6 hour ago" should "be idle" in {
    val hours = 6
    val pullRequest = PullRequestFactory.build(updated_at = TimeProviderStub.now().minusHours(hours))
    val pullRequests = new IdlePullRequestFilterImpl(TimeProviderStub).filter(List(pullRequest))
    pullRequests.size shouldBe 1
  }

  "Update 1 hour ago" should "not be idle" in {
    val hours = 1
    val pullRequest = PullRequestFactory.build(updated_at = TimeProviderStub.now().minusHours(hours))
    val pullRequests = new IdlePullRequestFilterImpl(TimeProviderStub).filter(List(pullRequest))
    pullRequests.size shouldBe 0
  }

  "Update 36hrs ago" should "not be idle" in {
    val hours = 36
    val pullRequest = PullRequestFactory.build(updated_at = TimeProviderStub.now().minusHours(hours))
    val pullRequests = new IdlePullRequestFilterImpl(TimeProviderStub).filter(List(pullRequest))
    pullRequests.size shouldBe 0
  }

  "Update 72hrs ago" should "be idle" in {
    val hours = 72
    val pullRequest = PullRequestFactory.build(updated_at = TimeProviderStub.now().minusHours(hours))
    val pullRequests = new IdlePullRequestFilterImpl(TimeProviderStub).filter(List(pullRequest))
    pullRequests.size shouldBe 1
  }

  "Update 9 days ago" should "be idle" in {
    val days = 9
    val pullRequest = PullRequestFactory.build(updated_at = TimeProviderStub.now().minusDays(days))
    val pullRequests = new IdlePullRequestFilterImpl(TimeProviderStub).filter(List(pullRequest))
    pullRequests.size shouldBe 1
  }

  "Update 80 days ago" should "not be idle" in {
    val days = 80
    val pullRequest = PullRequestFactory.build(updated_at = TimeProviderStub.now().minusDays(days))
    val pullRequests = new IdlePullRequestFilterImpl(TimeProviderStub).filter(List(pullRequest))
    pullRequests.size shouldBe 0
  }

  "Update 81 days ago" should "be idle" in {
    val days = 81
    val pullRequest = PullRequestFactory.build(updated_at = TimeProviderStub.now().minusDays(days))
    val pullRequests = new IdlePullRequestFilterImpl(TimeProviderStub).filter(List(pullRequest))
    pullRequests.size shouldBe 1
  }

}
