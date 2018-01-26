package factories

import java.time.{Clock, LocalDateTime}

import domain.GitHub.{Issue, Links, PullRequest, Repo}
import gateways.testDoubles.TimeProviderStub

object PullRequestFactory {

  def build(
    html_url: String = "http://",
    title: String = "Title",
    created_at: LocalDateTime = TimeProviderStub.now(),
    updated_at: LocalDateTime = TimeProviderStub.now(),
    _links: Links = new Links(new Issue(""))
  ): PullRequest = {
    new PullRequest(
      title = title,
      html_url = html_url,
      created_at = created_at,
      updated_at = updated_at,
      user = MemberFactory.build(),
      requested_reviewers = List(MemberFactory.build()),
      _links = _links
    )
  }

}

object RepoFactory {

  def build(url: String = "http://"): Repo = {
    new Repo(url = url)
  }

}