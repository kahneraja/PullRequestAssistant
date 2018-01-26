package gateways

import domain.GitHub.{Event, PullRequest, Repo}

import scala.concurrent.Future

trait GitHubGateway {
  def getEvents(str: String): Future[List[Event]]
  def getPullRequests(url: String): Future[List[PullRequest]]
  def getRepos(): Future[List[Repo]]
}
