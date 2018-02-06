package gateways

import gateways.testDoubles.{GatewayConfigStub, HttpClientSpy, HttpClientStub, TimeProviderStub}
import play.api.libs.json.Json

class SlackGatewayTest extends BaseSpec {

  "SlackGateway" should "post a new message" in {
    val httpClientSpy = new HttpClientSpy(None, None)
    val gateway = new SlackGatewayImpl(httpClientSpy, GatewayConfigStub)

    val expected = Json.parse(
      """{
        |"channel":"@john",
        |"text":"hello john!",
        |"as_user":"stubBotName"
        |}""".stripMargin)

    whenReady(gateway.postMessage("john", "hello john!")) { _ =>
      httpClientSpy.wasPostedWith.get shouldBe expected
    }
  }

  "SlackGateway" should "transform members" in {
    val sampleJson = Some(
      """
        |{
        |  "members": [
        |    {
        |        "id": "1",
        |        "real_name": "a",
        |        "profile": {
        |           "real_name": "A B"
        |        }
        |    }
        |  ]
        |}
      """.stripMargin)
    val httpClient = new HttpClientStub()
    httpClient.stubbedJson = sampleJson

    val gateway = new SlackGatewayImpl(httpClient, GatewayConfigStub)

    val token = ""
    whenReady(gateway.getMembers(token)) { members =>
      members.size shouldBe 1
    }
  }

}
