package repositories

import java.time.temporal.ChronoUnit

import domain.PullRequest

import gateways.TimeProvider

class RecentlyCreatedPullRequestFilterImpl(timeProvider: TimeProvider) extends PullRequestFilter {
  override def filter(pullRequests: List[PullRequest]): List[PullRequest] = {

    pullRequests.filter { pullRequest =>
      val hoursSinceUpdated = pullRequest.created_at.until(timeProvider.now(), ChronoUnit.HOURS).toInt
      hoursSinceUpdated == 0
    }
  }

}
