package gateways

import javax.inject.Inject

import domain.GitHub._
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class GitHubGatewayImpl @Inject()(
  httpClient: HttpClient,
  config: GatewayConfig,
  timeProvider: TimeProvider
) extends GitHubGateway {

  def createAccessToken(authTokenRequest: AuthTokenRequest): Future[AuthTokenResponse] = {
    val url = s"https://github.com/login/oauth/access_token"

    val headers: (String, String) = {
      "Accept" -> "application/json"
    }

    httpClient.post(url, Json.toJsObject(authTokenRequest), headers)
      .map { jsValue ⇒
        jsValue.as[AuthTokenResponse]
      }
  }

  def getEvents(url: String): Future[List[Event]] = {
    val headers: (String, String) = {
      "Authorization" -> s"token ${config.githubToken}"
    }

    httpClient.get(url, headers)
      .map { jsValue ⇒
        jsValue.as[List[Event]]
      }
  }

  def getPullRequests(url: String, state: String, perPage: Int): Future[List[PullRequest]] = {
    val headers: (String, String) = {
      "Authorization" -> s"token ${config.githubToken}"
    }

    httpClient.get(s"$url?state=$state&per_page=$perPage", headers)
      .map { jsValue ⇒
        jsValue.as[List[PullRequest]]
      }
  }

  def getTeamMembers(url: String): Future[List[Member]] = {

    val headers: (String, String) = {
      "Authorization" -> s"token ${config.githubToken}"
    }

    httpClient.get(url, headers)
      .map { jsValue ⇒
        jsValue.as[List[Member]]
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

  def getFiles(url: String): Future[List[File]] = {
    val headers: (String, String) = {
      "Authorization" -> s"token ${
        config.githubToken
      }"
    }

    httpClient.get(url, headers)
      .map {
        jsValue ⇒
          jsValue.as[List[File]]
      }
  }

  def getComments(url: String): Future[List[Comment]] = {
    val headers: (String, String) = {
      "Authorization" -> s"token ${
        config.githubToken
      }"
    }

    httpClient.get(url, headers)
      .map {
        jsValue ⇒
          jsValue.as[List[Comment]]
      }
  }
}
