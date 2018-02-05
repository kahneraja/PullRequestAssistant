package gateways

import domain.Slack.{AuthTokenRequest, AuthTokenResponse}
import play.api.libs.json.JsValue

import scala.concurrent.Future

trait SlackGateway {
  def createAccessToken(authTokenRequest: AuthTokenRequest): Future[AuthTokenResponse]
  def postMessage(channel: String, text: String): Future[JsValue]
}
