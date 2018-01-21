package gateways.testDoubles

import gateways.GatewayConfig

object GatewayConfigStub extends GatewayConfig {
  override val githubOrg: String = "stubGithubOrg"
  override val githubToken: String = "stubGithubToken"
  override val slackToken: String = "stubSlackToken"
  override val botName: String = "stubBotName"
}
