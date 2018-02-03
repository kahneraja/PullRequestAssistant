package useCases

import factories._
import gateways.testDoubles.TimeProviderStub
import gateways.{BaseSpec, GitHubGateway, SlackGateway}
import org.mockito.Matchers.any
import org.mockito.Mockito.{times, verify, when}
import repositories.ContributorRepository

import scala.concurrent.Future

class NotifyRecentlyAssignedUseCaseTest extends BaseSpec {

  "When successful" should "notify one user" in {

    val minutes = 30
    val events = List(EventFactory.build(
      event = "review_requested",
      requested_team = None,
      requested_reviewer = Some(MemberFactory.build()),
      review_requester = Some(MemberFactory.build()),
      created_at = TimeProviderStub.now().minusMinutes(minutes)
    ))

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
    when(gitHubGateway.getEvents(any[String]()))
      .thenReturn(Future.successful(events))

    new NotifyRecentlyAssignedUseCase(
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

    val minutes = 30
    val events = List(EventFactory.build(
      event = "review_requested",
      requested_team = Some(TeamFactory.build()),
      requested_reviewer = None,
      review_requester = Some(MemberFactory.build()),
      created_at = TimeProviderStub.now().minusMinutes(minutes)
    ))
    val pullRequests = List(PullRequestFactory.build(
      requested_reviewers = List(MemberFactory.build())
    ))
    val teamMembers = List(MemberFactory.build(), MemberFactory.build())

    when(gitHubGateway.getRepos())
      .thenReturn(Future.successful(List(RepoFactory.build())))
    when(gitHubGateway.getPullRequests(any[String](), any[String](), any[Int]()))
      .thenReturn(Future.successful(pullRequests))
    when(contributorRepository.find(any[String]()))
      .thenReturn(Future.successful(Some(ContributorFactory.build())))
    when(gitHubGateway.getEvents(any[String]()))
      .thenReturn(Future.successful(events))
    when(gitHubGateway.getTeamMembers(any[String]()))
      .thenReturn(Future.successful(teamMembers))

    new NotifyRecentlyAssignedUseCase(
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
