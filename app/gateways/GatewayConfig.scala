package gateways

import javax.inject._
import play.api.Configuration

trait GatewayConfig {
  val githubOrg: String
  val githubToken: String
  val slackToken: String
  val botName: String
}

class GatewayConfigImpl @Inject() (config: Configuration) extends GatewayConfig {
  val githubOrg: String = config.get[String]("githubOrg")
  val githubToken: String = config.get[String]("githubToken")
  val slackToken: String = config.get[String]("slackToken")
  val botName: String = config.get[String]("botName")
}
