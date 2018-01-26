package useCases

import factories.{EventFactory, MemberFactory, NotificationMessageFactory, TeamFactory}
import gateways.BaseSpec
import gateways.testDoubles.{GitHubGatewayStub, SlackGatewaySpy, TimeProviderStub, UserRepositoryStub}

class NotifyRecentlyAssignedUseCaseTest extends BaseSpec {

  "When successful" should "notify one user" in {

    val slackGatewaySpy = new SlackGatewaySpy()
    val gitHubGatewayStub = new GitHubGatewayStub()
    gitHubGatewayStub.stubbedEvents = List(EventFactory.build(
      event = "review_requested",
      requested_team = None,
      requested_reviewer = Some(MemberFactory.build()),
      review_requester = Some(MemberFactory.build())
    ))

    new NotifyRecentlyAssignedUseCase(
      slackGateway = slackGatewaySpy,
      gitHubGateway = gitHubGatewayStub,
      notificationMessageFactory = new NotificationMessageFactory(TimeProviderStub),
      userRepository = UserRepositoryStub,
      timeProvider = TimeProviderStub
    ).execute()

    eventually {
      slackGatewaySpy.messages shouldEqual List(("stub-slack-name","@stub-slack-name tagged you on a pr.\n*Title*\nhttp://"))
    }
  }

  "When successful" should "notify a team of users" in {

    val slackGatewaySpy = new SlackGatewaySpy()
    val gitHubGatewayStub = new GitHubGatewayStub()
    gitHubGatewayStub.stubbedEvents = List(EventFactory.build(
      event = "review_requested",
      requested_team = Some(TeamFactory.build()),
      requested_reviewer = None,
      review_requester = Some(MemberFactory.build())
    ))

    new NotifyRecentlyAssignedUseCase(
      slackGateway = slackGatewaySpy,
      gitHubGateway = gitHubGatewayStub,
      notificationMessageFactory = new NotificationMessageFactory(TimeProviderStub),
      userRepository = UserRepositoryStub,
      timeProvider = TimeProviderStub
    ).execute()

    eventually {
      slackGatewaySpy.messages shouldEqual List(
        ("stub-slack-name","@stub-slack-name tagged you on a pr.\n*Title*\nhttp://"),
        ("stub-slack-name","@stub-slack-name tagged you on a pr.\n*Title*\nhttp://")
      )
    }
  }

}
