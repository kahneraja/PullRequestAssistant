package gateways.testDoubles

import domain.PullRequest
import repositories.PullRequestFilter

object PullRequestFilterStub extends PullRequestFilter {
  override def filter(pullRequests: List[PullRequest]): List[PullRequest] = {
    return pullRequests
  }
}
