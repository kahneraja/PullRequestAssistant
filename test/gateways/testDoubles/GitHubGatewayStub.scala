package gateways.testDoubles

import domain.GitHub.{Event, Member, PullRequest, Repo}
import factories.{EventFactory, MemberFactory, PullRequestFactory, RepoFactory}
import gateways.GitHubGateway

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GitHubGatewayStub extends GitHubGateway {

  var stubbedTeamMembers = List(
    MemberFactory.build(),
    MemberFactory.build()
  )
  var stubbedEvents: List[Event] = List(EventFactory.build())
  var stubbedPullRequests: List[PullRequest] = List(PullRequestFactory.build())

  override def getPullRequests(url: String): Future[List[PullRequest]] = {
    Future {
      stubbedPullRequests
    }
  }

  override def getRepos(): Future[List[Repo]] = {
    Future {
      List(RepoFactory.build())
    }
  }

  override def getEvents(str: String): Future[List[Event]] = {
    Future {
      stubbedEvents
    }
  }

  override def getTeamMembers(str: String): Future[List[Member]] = {
    Future {
      stubbedTeamMembers
    }
  }
}
