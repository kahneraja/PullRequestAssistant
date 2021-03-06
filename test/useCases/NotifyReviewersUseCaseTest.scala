package useCases

import factories._
import gateways.testDoubles.TimeProviderStub
import gateways.{BaseSpec, GitHubGateway, SlackGateway}
import org.mockito.Matchers._
import org.mockito.Mockito._
import repositories.ContributorRepository

import scala.concurrent.Future

class NotifyReviewersUseCaseTest extends BaseSpec {

  "When successful" should "notify one user" in {

    val contributorRepository = mock[ContributorRepository]
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
    when(contributorRepository.find(any[String]()))
      .thenReturn(Future.successful(Some(ContributorFactory.build())))


    val useCase = new NotifyReviewersUseCase(
      slackGateway = slackGateway,
      gitHubGateway = gitHubGateway,
      notificationMessageFactory = new NotificationMessageFactory(TimeProviderStub),
      contributorRepository = contributorRepository,
      timeProvider = TimeProviderStub
    ).execute()

    eventually {
      verify(slackGateway, times(1)).postMessage(any[String], any[String])
    }
  }

  "When successful" should "notify a team of contributors" in {

    val contributorRepository = mock[ContributorRepository]
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
    when(gitHubGateway.getPullRequests(any[String](), any[String](), any[Int]()))
      .thenReturn(Future.successful(pullRequests))
    when(gitHubGateway.getTeamMembers(any[String]()))
      .thenReturn(Future.successful(teamMembers))
    when(contributorRepository.find(any[String]()))
      .thenReturn(Future.successful(Some(ContributorFactory.build())))

    val useCase = new NotifyReviewersUseCase(
      slackGateway = slackGateway,
      gitHubGateway = gitHubGateway,
      notificationMessageFactory = new NotificationMessageFactory(TimeProviderStub),
      contributorRepository = contributorRepository,
      timeProvider = TimeProviderStub
    ).execute()

    eventually {
      verify(slackGateway, times(2)).postMessage(any[String], any[String])
    }
  }

}
