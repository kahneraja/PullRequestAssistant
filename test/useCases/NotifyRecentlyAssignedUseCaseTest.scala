package useCases

import factories.NotificationMessageFactory
import gateways.BaseSpec
import gateways.testDoubles._

class NotifyRecentlyAssignedUseCaseTest extends BaseSpec {

  "NotifyRecentlyAssignedCaseTest" should "notifies all recently assigned reviewers" in {

    val slackGatewaySpy = new SlackGatewaySpy()

    val useCase = new NotifyRecentlyAssignedUseCase(
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
