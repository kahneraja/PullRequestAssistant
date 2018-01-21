package useCases

import factories.NotificationMessageFactory
import gateways.BaseSpec
import gateways.testDoubles._

class NotifyReviewersUseCaseTest extends BaseSpec {

  "NotifyReviewersCaseTest" should "notifies all assigned reviewers on idle pull requests." in {

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
