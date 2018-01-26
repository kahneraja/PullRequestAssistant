package gateways

import domain.GitHub.{Event, Member, PullRequest, Repo}

import scala.concurrent.Future

trait GitHubGateway {
  def getEvents(url: String): Future[List[Event]]
  def getPullRequests(url: String): Future[List[PullRequest]]
  def getRepos(): Future[List[Repo]]
  def getTeamMembers(url: String): Future[List[Member]]
}
