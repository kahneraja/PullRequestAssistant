package gateways

import play.api.libs.json.{JsObject, JsValue}

import scala.concurrent.Future

trait HttpClient {
  def post(url: String, body: JsObject, headers: (String, String)*)
  def get(url: String, headers: (String, String)*): Future[JsValue]
}
