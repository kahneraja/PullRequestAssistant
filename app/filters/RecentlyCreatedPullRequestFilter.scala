package filters

import java.time.temporal.ChronoUnit

import domain.GitHub.PullRequest
import gateways.TimeProvider

object RecentlyCreatedPullRequestFilter  {
  def filter(pullRequests: List[PullRequest], timeProvider: TimeProvider): List[PullRequest] = {
    pullRequests.filter { pullRequest =>
      val hoursSinceUpdated = pullRequest.created_at.until(timeProvider.now(), ChronoUnit.HOURS).toInt
      hoursSinceUpdated == 0
    }
  }

}
