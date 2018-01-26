package gateways

import javax.inject.Inject

import play.api.libs.json._
import play.api.libs.ws.JsonBodyReadables._
import play.api.libs.ws.JsonBodyWritables._
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class HttpClientImpl @Inject()(ws: WSClient) extends HttpClient {

  def post(url: String, body: JsObject, headers: (String, String)*): Future[JsValue] = {
    ws
      .url(url)
      .withHttpHeaders(headers: _*)
      .post(body)
      .map { wsresponse ⇒
        Logger.log(s"post $url -> ${wsresponse.status} $body")
        wsresponse.body[JsValue]
      }
  }

  def get(url: String, headers: (String, String)*): Future[JsValue] = {
    ws
      .url(url)
      .withHttpHeaders(headers: _*)
      .get()
      .map { wsresponse ⇒
        Logger.log(s"get $url -> ${wsresponse.status}")
        wsresponse.body[JsValue]
      }
  }
}
