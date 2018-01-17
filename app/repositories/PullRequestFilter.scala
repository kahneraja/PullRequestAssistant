package repositories

import domain.PullRequest

trait PullRequestFilter {

  def filter(pullRequests: List[PullRequest]): List[PullRequest]

}
