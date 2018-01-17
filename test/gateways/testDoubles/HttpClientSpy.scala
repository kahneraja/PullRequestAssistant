package gateways.testDoubles

import gateways.HttpClient
import play.api.libs.json.{JsObject, JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class HttpClientSpy(
  var wasPostedWith: Option[JsObject],
  var didGetUrl: Option[String]
) extends HttpClient {

  override def post(url: String, body: JsObject, headers: (String, String)*) = {
    wasPostedWith = Some(body)
  }

  override def get(url: String, headers: (String, String)*): Future[JsValue] = {
    didGetUrl = Some(url)
    Future {
      Json.parse("""{"stub":"data"}""")
    }
  }
}
