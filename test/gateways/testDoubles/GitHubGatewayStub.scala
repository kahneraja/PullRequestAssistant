package gateways.testDoubles

import domain.{PullRequest, Repo}
import factories.{PullRequestFactory, RepoFactory}
import gateways.GitHubGateway

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object GitHubGatewayStub extends GitHubGateway {

  override def getPullRequests(url: String): Future[List[PullRequest]] = {
    Future {
      List(PullRequestFactory.build())
    }
  }

  override def getRepos(): Future[List[Repo]] = {
    Future {
      List(RepoFactory.build())
    }
  }

}
