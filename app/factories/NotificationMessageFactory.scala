package factories

import domain.{Member, PullRequest}

import gateways.TimeProvider

class NotificationMessageFactory(timeProvider: TimeProvider) {
  def buildRecentlyAssignedMessage(pullRequest: PullRequest, owner: Member): String = {
    val hours = pullRequest.getHoursSinceUpdated(timeProvider)
    val title = pullRequest.title
    val url = pullRequest.html_url

    s"@${owner.slack_name} tagged you on a new pr.\n*$title*\n$url"
  }

  def buildReviewMessage(pullRequest: PullRequest, owner: Member): String = {
    val hours = pullRequest.getHoursSinceUpdated(timeProvider)
    val title = pullRequest.title
    val url = pullRequest.html_url

    s"@${owner.slack_name}'s pr has now been idle for ${hours}hrs.\n*$title*\n$url"
  }

  def buildOwnerMessage(pullRequest: PullRequest, owner: Member): String = {
    val hours = pullRequest.getHoursSinceUpdated(timeProvider)
    val title = pullRequest.title
    val url = pullRequest.html_url

    s"... fyi this one has now been idle for ${hours}hrs.\n*$title*\n$url"
  }

}
