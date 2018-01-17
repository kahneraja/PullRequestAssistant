package gateways

trait SlackGateway {
  def postMessage(channel: String, text: String)
}
