package gateways

import play.api.libs.json.JsValue

import scala.concurrent.Future

trait SlackGateway {
  def postMessage(channel: String, text: String): Future[JsValue]
}
