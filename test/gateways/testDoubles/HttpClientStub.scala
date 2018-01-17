package gateways.testDoubles

import gateways.HttpClient
import play.api.libs.json.{JsObject, JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class HttpClientStub extends HttpClient {

  var stubbedJson: Option[String] = None

  override def post(url: String, body: JsObject, headers: (String, String)*) = {
    throw new NotImplementedError()
  }

  override def get(url: String, headers: (String, String)*): Future[JsValue] = {
    Future {
      Json.parse(stubbedJson.get)
    }
  }
}
