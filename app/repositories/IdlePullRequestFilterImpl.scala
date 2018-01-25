package repositories

import java.time.temporal.ChronoUnit

import domain.PullRequest

import gateways.TimeProvider

class IdlePullRequestFilterImpl(timeProvider: TimeProvider) extends PullRequestFilter {

  override def filter(pullRequests: List[PullRequest]): List[PullRequest] = {
    pullRequests.filter { pullRequest =>
      val hoursSinceUpdated = pullRequest.updated_at.until(timeProvider.now(), ChronoUnit.HOURS).toInt
      isIdleToday(hoursSinceUpdated) || isIdleEver(hoursSinceUpdated)
    }
  }

  private def isIdleToday(hoursSinceUpdated: Int) = {
    val SIXHOURS = 6
    val ONEDAY = 24
    val checkPoints = List(SIXHOURS, ONEDAY)
    checkPoints.contains(hoursSinceUpdated)
  }

  private def isIdleEver(hoursSinceUpdated: Int) = {
    val threeDaySplit = 24 * 3
    hoursSinceUpdated > 0 && hoursSinceUpdated % threeDaySplit == 0
  }
}
