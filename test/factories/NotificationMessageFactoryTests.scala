package factories

import gateways.BaseSpec
import gateways.testDoubles.TimeProviderStub

class NotificationMessageFactoryTests extends BaseSpec {

  "NotificationMessage" should "build reviewer message." in {
    val pullRequest = PullRequestFactory.build(
      html_url = "http://github.com/pr/123",
      title = "Fix defect ABC",
      updated_at = TimeProviderStub.now().minusHours(2)
    )
    val owner = UserFactory.build(slack_name = "john")
    val message = new NotificationMessageFactory(TimeProviderStub)
      .buildReviewMessage(
        pullRequest,
        owner = owner)
    message shouldBe "@john's pr has now been idle for 2hrs.\n*Fix defect ABC*\nhttp://github.com/pr/123"
  }

  "NotificationMessage" should "build owner message." in {
    val pullRequest = PullRequestFactory.build(
      html_url = "http://github.com/pr/123",
      title = "Fix defect ABC",
      updated_at = TimeProviderStub.now().minusHours(2)
    )
    val owner = UserFactory.build(slack_name = "john")
    val message = new NotificationMessageFactory(TimeProviderStub)
      .buildOwnerMessage(
        pullRequest,
        owner = owner)
    message shouldBe "I let your reviewers know this one has been idle for 2hrs.\n*Fix defect ABC*\nhttp://github.com/pr/123"
  }

  "NotificationMessage" should "build recently assigned message." in {
    val event = EventFactory.build()
    val pullRequest = PullRequestFactory.build(
      html_url = "http://github.com/pr/123",
      title = "Fix defect ABC",
      updated_at = TimeProviderStub.now().minusHours(2)
    )
    val owner = UserFactory.build(slack_name = "john")
    val message = new NotificationMessageFactory(TimeProviderStub)
      .buildRecentlyAssignedMessage(
        event,
        pullRequest,
        owner = owner)
    message shouldBe "@john tagged you on a pr.\n*Fix defect ABC*\nhttp://github.com/pr/123"
  }

}
