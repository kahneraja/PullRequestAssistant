package useCases

import factories.NotificationMessageFactory
import gateways.testDoubles._
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}

class NotifyReviewersUseCaseTest  extends FlatSpec with Matchers with ScalaFutures {

  implicit override val patienceConfig = PatienceConfig(
    timeout = Span(5, Seconds),
    interval = Span(500, Millis)
  )

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
