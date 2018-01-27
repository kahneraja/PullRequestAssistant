package gateways

import domain.GitHub.{Event, Member, PullRequest, Repo}

import scala.concurrent.Future

trait GitHubGateway {
  val PER_PAGE: Int = 100

  def getEvents(url: String): Future[List[Event]]

  def getPullRequests(url: String, state: String = "open", perPage: Int = PER_PAGE): Future[List[PullRequest]]

  def getRepos(): Future[List[Repo]]

  def getTeamMembers(url: String): Future[List[Member]]
}
