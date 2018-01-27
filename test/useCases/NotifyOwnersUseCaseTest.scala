package useCases

import factories._
import gateways.testDoubles.{TimeProviderStub}
import gateways.{BaseSpec, GitHubGateway, SlackGateway}
import org.mockito.Matchers.any
import org.mockito.Mockito.{times, verify, when}
import repositories.UserRepository

import scala.concurrent.Future

class NotifyOwnersUseCaseTest extends BaseSpec {

  "When successful" should "notify one user" in {
    val userRepository = mock[UserRepository]
    val slackGateway = mock[SlackGateway]
    val gitHubGateway = mock[GitHubGateway]
    val hours = 6
    val pullRequests = List(PullRequestFactory.build(
      updated_at = TimeProviderStub.now.minusHours(hours),
      requested_reviewers = List(MemberFactory.build())
    ))

    when(gitHubGateway.getRepos())
      .thenReturn(Future.successful(List(RepoFactory.build())))
    when(gitHubGateway.getPullRequests(any[String](), any[String](), any[Int]()))
      .thenReturn(Future.successful(pullRequests))
    when(userRepository.findUser(any[String]()))
      .thenReturn(Future.successful(Some(UserFactory.build())))

    new NotifyOwnersUseCase(
      slackGateway = slackGateway,
      gitHubGatway = gitHubGateway,
      notificationMessageFactory = new NotificationMessageFactory(TimeProviderStub),
      userRepository = userRepository,
      timeProvider = TimeProviderStub
    ).execute()

    eventually {
      verify(slackGateway, times(1)).postMessage(any[String], any[String])
    }
  }

}
