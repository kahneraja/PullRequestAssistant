package gateways

import domain.GitHub.{PullRequest, Repo}

import scala.concurrent.Future

trait GitHubGateway {
  def getPullRequests(url: String): Future[List[PullRequest]]
  def getRepos(): Future[List[Repo]]
}
