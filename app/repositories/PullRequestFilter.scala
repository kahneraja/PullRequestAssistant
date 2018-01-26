package repositories

import domain.GitHub.PullRequest

trait PullRequestFilter {

  def filter(pullRequests: List[PullRequest]): List[PullRequest]

}
