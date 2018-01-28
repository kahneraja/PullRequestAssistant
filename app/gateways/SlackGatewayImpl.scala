package gateways

import javax.inject.Inject

import play.api.libs.json.{JsValue, Json}

import scala.concurrent.Future

class SlackGatewayImpl @Inject()(
  httpClient: HttpClient,
  config: GatewayConfig
) extends SlackGateway {

  def postMessage(channel: String, text: String): Future[JsValue] = {
    val headers: (String, String) = {
      "Authorization" -> s"Bearer ${config.slackToken}"
    }

    val body = Json.obj(
      "channel" -> s"@$channel",
      "text" -> text,
      "as_user" -> config.botName
    )

    httpClient.post("https://slack.com/api/chat.postMessage", body, headers)
  }

}
