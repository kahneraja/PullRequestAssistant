package gateways

import gateways.testDoubles.{GatewayConfigStub, HttpClientStub, TimeProviderStub}

class GitHubGatewayTest extends BaseSpec {

  "GitHubGateway" should "transform pull request json" in {
    val sampleJson = Some(
      """
        |[
        |  {
        |    "url": "https://stub",
        |    "html_url": "https://stub",
        |    "title": "Pull Request 1",
        |    "user": {
        |      "login": "stub-login",
        |      "url": "http://stub"
        |    },
        |    "body": "stub body",
        |    "created_at": "2018-01-01T00:00:00Z",
        |    "updated_at": "2018-01-01T00:00:00Z",
        |    "requested_reviewers": [
        |      {
        |        "login": "stub-login",
        |        "url": "http://stub"
        |      }
        |    ],
        |    "requested_teams": [
        |       {
        |         "name": "team1",
        |         "url": "https://"
        |       }
        |    ],
        |    "_links": {
        |      "issue": {
        |        "href": "https://"
        |      }
        |    }
        |  }
        |]
      """.stripMargin)
    val httpClient = new HttpClientStub()
    httpClient.stubbedJson = sampleJson

    val gateway = new GitHubGatewayImpl(httpClient, GatewayConfigStub, TimeProviderStub)

    whenReady(gateway.getPullRequests("")) { pullRequests =>
      pullRequests.size shouldBe 1
    }
  }

  "GitHubGateway" should "transform issue events" in {
    val sampleJson = Some(
      """
        |[
        |    {
        |        "url": "http://",
        |        "actor": {
        |            "login": "abx",
        |            "url": "https://"
        |        },
        |        "event": "event abc",
        |        "created_at": "2018-01-01T00:00:00Z",
        |        "review_requester": {
        |            "login": "Xodia",
        |            "url": "https://"
        |        },
        |        "requested_reviewer": {
        |            "login": "abx",
        |            "url": "https://"
        |        },
        |        "requested_team": {
        |            "name": "team1",
        |            "url": "https://"
        |        }
        |    }
        |]
      """.stripMargin)
    val httpClient = new HttpClientStub()
    httpClient.stubbedJson = sampleJson

    val gateway = new GitHubGatewayImpl(httpClient, GatewayConfigStub, TimeProviderStub)

    whenReady(gateway.getEvents("")) { events =>
      events.size shouldBe 1
    }
  }

  "GitHubGateway" should "transform a team" in {
    val sampleJson = Some(
      """
        |[
        |  {
        |    "login": "stub-login",
        |    "url": "http://stub"
        |  }
        |]
      """.stripMargin)
    val httpClient = new HttpClientStub()
    httpClient.stubbedJson = sampleJson

    val gateway = new GitHubGatewayImpl(httpClient, GatewayConfigStub, TimeProviderStub)

    whenReady(gateway.getTeamMembers("")) { members =>
      members.size shouldBe 1
    }
  }

}
