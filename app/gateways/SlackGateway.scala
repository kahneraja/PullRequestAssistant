package gateways

import domain.Slack.{AuthTokenRequest, AuthTokenResponse, Member}
import play.api.libs.json.JsValue

import scala.concurrent.Future

trait SlackGateway {
  def getMembers(token: String): Future[List[Member]]
  def createAccessToken(authTokenRequest: AuthTokenRequest): Future[AuthTokenResponse]
  def postMessage(channel: String, text: String): Future[JsValue]
}
