package factories

import gateways.testDoubles.TimeProviderStub
import org.scalatest._

class NotificationMessageFactoryTests extends FlatSpec with Matchers {

  "NotificationMessage" should "build reviewer message." in {
    val pullRequest = PullRequestFactory.build(
      html_url = "http://github.com/pr/123",
      title = "Fix defect ABC",
      updated_at = TimeProviderStub.now().minusHours(2)
    )
    val owner = MemberFactory.build(slack_name = "john")
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
    val owner = MemberFactory.build(slack_name = "john")
    val message = new NotificationMessageFactory(TimeProviderStub)
      .buildOwnerMessage(
        pullRequest,
        owner = owner)
    message shouldBe "... fyi this one has now been idle for 2hrs.\n*Fix defect ABC*\nhttp://github.com/pr/123"
  }

  "NotificationMessage" should "build recently assigned message." in {
    val pullRequest = PullRequestFactory.build(
      html_url = "http://github.com/pr/123",
      title = "Fix defect ABC",
      updated_at = TimeProviderStub.now().minusHours(2)
    )
    val owner = MemberFactory.build(slack_name = "john")
    val message = new NotificationMessageFactory(TimeProviderStub)
      .buildRecentlyAssignedMessage(
        pullRequest,
        owner = owner)
    message shouldBe "@john tagged you on a new pr.\n*Fix defect ABC*\nhttp://github.com/pr/123"
  }

}
