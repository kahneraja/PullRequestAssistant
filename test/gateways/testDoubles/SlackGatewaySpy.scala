package gateways.testDoubles

import gateways.SlackGateway
import play.api.libs.json.{JsObject, Json}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class SlackGatewaySpy extends SlackGateway {

  var messages: List[(String, String)] = List()

  def postMessage(channel: String, text: String): Future[JsObject] = {
    println("postMessage " + messages.size)
    messages = messages:+((channel, text))
    Future {
      Json.obj()
    }
  }

}
