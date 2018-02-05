package gateways

import javax.inject.Inject

import domain.Slack.{AuthTokenRequest, AuthTokenResponse}
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits._
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

  def createAccessToken(request: AuthTokenRequest): Future[AuthTokenResponse] = {
    val api = "https://slack.com/api/oauth.access"
    val url = s"${api}?client_id=${request.client_id}&client_secret=${request.client_secret}&code=${request.code}&redirect_uri=${request.redirect_uri}"

    val headers: (String, String) = {
      "Accept" -> "application/json"
    }

    httpClient.post(url, Json.obj(), headers)
      .map { jsValue ⇒
        jsValue.as[AuthTokenResponse]
      }
  }

}
