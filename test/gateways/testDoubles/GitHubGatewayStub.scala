package gateways.testDoubles

import domain.GitHub.{Event, PullRequest, Repo}
import factories.{EventFactory, PullRequestFactory, RepoFactory}
import gateways.GitHubGateway

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GitHubGatewayStub extends GitHubGateway {

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
}
