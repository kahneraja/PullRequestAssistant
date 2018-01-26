package useCases

import factories._
import gateways.{BaseSpec, GitHubGateway, SlackGateway}
import gateways.testDoubles.{GitHubGatewayStub, SlackGatewaySpy, TimeProviderStub, UserRepositoryStub}
import org.mockito.Matchers._
import org.mockito.Mockito._
import repositories.UserRepository

import scala.concurrent.Future

class NotifyReviewersUseCaseTest extends BaseSpec {

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
    when(gitHubGateway.getPullRequests(any[String]()))
      .thenReturn(Future.successful(pullRequests))
    when(userRepository.findUser(any[String]()))
      .thenReturn(Future.successful(Some(UserFactory.build())))


    val useCase = new NotifyReviewersUseCase(
      slackGateway = slackGateway,
      gitHubGateway = gitHubGateway,
      notificationMessageFactory = new NotificationMessageFactory(TimeProviderStub),
      userRepository = userRepository,
      timeProvider = TimeProviderStub
    ).execute()

    eventually {
      verify(slackGateway, times(1)).postMessage(any[String], any[String])
    }
  }

  "When successful" should "notify a team of users" in {

    val userRepository = mock[UserRepository]
    val slackGateway = mock[SlackGateway]
    val gitHubGateway = mock[GitHubGateway]
    val hours = 6
    val pullRequests = List(PullRequestFactory.build(
      updated_at = TimeProviderStub.now.minusHours(hours),
      requested_teams = List(TeamFactory.build()))
    )
    val teamMembers = List(MemberFactory.build(), MemberFactory.build())

    when(gitHubGateway.getRepos())
      .thenReturn(Future.successful(List(RepoFactory.build())))
    when(gitHubGateway.getPullRequests(any[String]()))
      .thenReturn(Future.successful(pullRequests))
    when(gitHubGateway.getTeamMembers(any[String]()))
      .thenReturn(Future.successful(teamMembers))
    when(userRepository.findUser(any[String]()))
      .thenReturn(Future.successful(Some(UserFactory.build())))

    val useCase = new NotifyReviewersUseCase(
      slackGateway = slackGateway,
      gitHubGateway = gitHubGateway,
      notificationMessageFactory = new NotificationMessageFactory(TimeProviderStub),
      userRepository = userRepository,
      timeProvider = TimeProviderStub
    ).execute()

    eventually {
      verify(slackGateway, times(2)).postMessage(any[String], any[String])
    }
  }

}
