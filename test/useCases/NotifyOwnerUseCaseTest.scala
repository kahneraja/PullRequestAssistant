package useCases

import factories.NotificationMessageFactory
import gateways.BaseSpec
import gateways.testDoubles._

class NotifyOwnerUseCaseTest extends BaseSpec {

  "NotifyOwnerUserCase" should "notifies owners of all idle pull requests." in {

    val slackGatewaySpy = new SlackGatewaySpy()

    val useCase = new NotifyReviewersUseCase(
      slackGateway = slackGatewaySpy,
      gitHubGatway = GitHubGatewayStub,
      notificationMessageFactory = new NotificationMessageFactory(TimeProviderStub),
      pullRequestFilter = PullRequestFilterStub,
      memberRepository = MemberRepositoryStub
    )

    whenReady(useCase.execute()) { _ =>
      slackGatewaySpy.messages.size shouldBe 1
    }
  }

}
