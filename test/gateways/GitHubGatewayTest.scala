package gateways

import gateways.testDoubles.{GatewayConfigStub, HttpClientStub, TimeProviderStub}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{Matchers, _}

class GitHubGatewayTest extends FlatSpec with Matchers with ScalaFutures {

  implicit override val patienceConfig = PatienceConfig(
    timeout = Span(2, Seconds),
    interval = Span(20, Millis)
  )

  val sampleJson = Some(
    """
      |[
      |  {
      |    "url": "https://stub",
      |    "id": 1,
      |    "html_url": "https://stub",
      |    "title": "Pull Request 1",
      |    "user": {
      |      "login": "stub-login",
      |      "id": 1,
      |      "url": "http://stub"
      |    },
      |    "body": "stub body",
      |    "created_at": "2018-01-01T00:00:00Z",
      |    "updated_at": "2018-01-01T00:00:00Z",
      |    "requested_reviewers": [
      |      {
      |        "login": "stub-login",
      |        "id": 9055741,
      |        "url": "http://stub"
      |      }
      |    ]
      |  },
      |  {
      |    "url": "https://stub",
      |    "id": 1,
      |    "html_url": "https://stub",
      |    "title": "Pull Request 2",
      |    "user": {
      |      "login": "stub-login",
      |      "id": 1,
      |      "url": "http://stub"
      |    },
      |    "body": "stub body (updated 24hrs ago)",
      |    "created_at": "2018-01-01T00:00:00Z",
      |    "updated_at": "2018-01-01T00:00:00Z",
      |    "requested_reviewers": [
      |      {
      |        "login": "stub-login",
      |        "id": 9055741,
      |        "url": "http://stub"
      |      }
      |    ]
      |  }
      |]
    """.stripMargin)

  "GitHubGateway" should "transform json and return all pull requests" in {
    val repoUrl = "http://stub"
    val httpClient = new HttpClientStub()
    httpClient.stubbedJson = sampleJson

    val gateway = new GitHubGatewayImpl(httpClient, GatewayConfigStub, TimeProviderStub)

    whenReady(gateway.getPullRequests(repoUrl)) { pullRequests =>
      pullRequests.size shouldBe 2
    }
  }

}
