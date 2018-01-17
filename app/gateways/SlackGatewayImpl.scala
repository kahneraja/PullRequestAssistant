package gateways

import play.api.libs.json.Json

class SlackGatewayImpl(httpClient: HttpClient, config: GatewayConfig) extends SlackGateway {

  def postMessage(channel: String, text: String): Unit = {
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
