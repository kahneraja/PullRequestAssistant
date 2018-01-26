package useCases

import factories.{EventFactory, NotificationMessageFactory}
import gateways.BaseSpec
import gateways.testDoubles.{GitHubGatewayStub, SlackGatewaySpy, TimeProviderStub, UserRepositoryStub}
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Seconds, Span}

class NotifyRecentlyAssignedUseCaseTest extends BaseSpec with Eventually {

  "When successful" should "notify one user" in {

    val slackGatewaySpy = new SlackGatewaySpy()
    val gitHubGatewayStub = new GitHubGatewayStub()
    gitHubGatewayStub.stubbedEvents = List(EventFactory.build(
      event = "review_requested"
    ))

    new NotifyRecentlyAssignedUseCase(
      slackGateway = slackGatewaySpy,
      gitHubGatway = gitHubGatewayStub,
      notificationMessageFactory = new NotificationMessageFactory(TimeProviderStub),
      userRepository = UserRepositoryStub,
      timeProvider = TimeProviderStub
    ).execute()

    eventually(timeout(Span(1, Seconds))) {
      slackGatewaySpy.messages shouldEqual List(("stub-slack-name","@stub-slack-name tagged you on a pr.\n*Title*\nhttp://"))
    }
  }

}
