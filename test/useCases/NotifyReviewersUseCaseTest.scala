package useCases

import factories.{NotificationMessageFactory, PullRequestFactory}
import gateways.BaseSpec
import gateways.testDoubles.{GitHubGatewayStub, SlackGatewaySpy, TimeProviderStub, UserRepositoryStub}
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Seconds, Span}

class NotifyReviewersUseCaseTest extends BaseSpec with Eventually {

  "When successful" should "notify one user" in {
    val slackGatewaySpy = new SlackGatewaySpy()
    val gitHubGatewayStub = new GitHubGatewayStub()
    val hours = 6
    gitHubGatewayStub.stubbedPullRequests = List(PullRequestFactory.build(updated_at = TimeProviderStub.now.minusHours(hours)))

    new NotifyReviewersUseCase(
      slackGateway = slackGatewaySpy,
      gitHubGatway = gitHubGatewayStub,
      notificationMessageFactory = new NotificationMessageFactory(TimeProviderStub),
      userRepository = UserRepositoryStub,
      timeProvider = TimeProviderStub
    ).execute()

    eventually(timeout(Span(1, Seconds))) {
      slackGatewaySpy.messages.size shouldBe 1
    }
  }

}
