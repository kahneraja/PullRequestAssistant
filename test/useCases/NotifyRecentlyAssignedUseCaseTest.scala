package useCases

import factories.NotificationMessageFactory
import gateways.testDoubles._
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}

class NotifyRecentlyAssignedUseCaseTest  extends FlatSpec with Matchers with ScalaFutures {

  implicit override val patienceConfig = PatienceConfig(
    timeout = Span(5, Seconds),
    interval = Span(500, Millis)
  )

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
