package gateways

import domain.{PullRequest, Repo}

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class GitHubGatewayImpl(
  httpClient: HttpClient,
  config: GatewayConfig,
  timeProvider: TimeProvider
) extends GitHubGateway {

  def getPullRequests(url: String): Future[List[PullRequest]] = {

    val headers: (String, String) = {
      "Authorization" -> s"token ${config.githubToken}"
    }

    httpClient.get(url, headers)
      .map { jsValue ⇒
        jsValue.as[List[PullRequest]]
      }
  }

  def getRepos(): Future[List[Repo]] = {
    val url = s"https://${config.githubOrg}/repos"
    val headers: (String, String) = {
      "Authorization" -> s"token ${
        config.githubToken
      }"
    }

    httpClient.get(url, headers)
      .map {
        jsValue ⇒
          jsValue.as[List[Repo]]
      }
  }

}
