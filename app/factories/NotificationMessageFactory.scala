package factories

import domain.GitHub.PullRequest
import domain.User
import gateways.TimeProvider

class NotificationMessageFactory(timeProvider: TimeProvider) {
  def buildRecentlyAssignedMessage(pullRequest: PullRequest, owner: User): String = {
    val hours = pullRequest.getHoursSinceUpdated(timeProvider)
    val title = pullRequest.title
    val url = pullRequest.html_url

    s"@${owner.slack_name} tagged you on a new pr.\n*$title*\n$url"
  }

  def buildReviewMessage(pullRequest: PullRequest, owner: User): String = {
    val hours = pullRequest.getHoursSinceUpdated(timeProvider)
    val title = pullRequest.title
    val url = pullRequest.html_url

    s"@${owner.slack_name}'s pr has now been idle for ${hours}hrs.\n*$title*\n$url"
  }

  def buildOwnerMessage(pullRequest: PullRequest, owner: User): String = {
    val hours = pullRequest.getHoursSinceUpdated(timeProvider)
    val title = pullRequest.title
    val url = pullRequest.html_url

    s"I let your reviewers know this one has been idle for ${hours}hrs.\n*$title*\n$url"
  }

}
