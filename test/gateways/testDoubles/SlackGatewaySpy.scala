package gateways.testDoubles

import gateways.SlackGateway

class SlackGatewaySpy extends SlackGateway {

  var messages: List[(String, String)] = List()

  def postMessage(channel: String, text: String) = {
    messages = messages:+((channel, text))
  }

}
