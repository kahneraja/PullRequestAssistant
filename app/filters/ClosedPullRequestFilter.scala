package filters

import domain.GitHub.PullRequest

object ClosedPullRequestFilter {
  def filter(pullRequests: List[PullRequest]): List[PullRequest] = {
    pullRequests.filter { pullRequest =>
      pullRequest.closed_at match {
        case Some(closed_at) => true
        case _ => false
      }
    }
  }
}
