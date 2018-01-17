package repositories

import java.time.temporal.ChronoUnit

import domain.PullRequest

import gateways.TimeProvider

class IdlePullRequestFilterImpl(timeProvider: TimeProvider) extends PullRequestFilter {
  override def filter(pullRequests: List[PullRequest]): List[PullRequest] = {
    val SIXHOURS = 6
    val ONEDAY = 24
    val THREEDAYS = 72
    val ONEWEEK = 168
    val FOURWEEKS = 336
    val checkPoints = List(SIXHOURS, ONEDAY, THREEDAYS, ONEWEEK, FOURWEEKS)
    pullRequests.filter { pullRequest =>
      val hoursSinceUpdated = pullRequest.updated_at.until(timeProvider.now(), ChronoUnit.HOURS).toInt
      checkPoints.contains(hoursSinceUpdated)
    }
  }

}
