package gateways

import gateways.testDoubles.{GatewayConfigStub, HttpClientSpy}
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

}
