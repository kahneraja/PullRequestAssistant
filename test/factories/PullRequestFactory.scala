package factories

import java.time.{Clock, LocalDateTime}

import domain.GitHub._
import gateways.testDoubles.TimeProviderStub

object PullRequestFactory {

  def build(
    url: String = "http://",
    html_url: String = "http://",
    title: String = "Title",
    created_at: LocalDateTime = TimeProviderStub.now(),
    updated_at: LocalDateTime = TimeProviderStub.now(),
    closed_at: Option[LocalDateTime] = None,
    _links: Links = new Links(new Href(""), new Href("")),
    requested_reviewers: List[Member] = List.empty,
    requested_teams: List[Team] = List.empty,
    files: Option[List[File]] = None,
    comments: Option[List[Comment]] = None
  ): PullRequest = {
    new PullRequest(
      title = title,
      url = url,
      html_url = html_url,
      created_at = created_at,
      updated_at = updated_at,
      closed_at = closed_at,
      user = MemberFactory.build(),
      _links = _links,
      requested_reviewers = requested_reviewers,
      requested_teams = requested_teams,
      files = files,
      comments = comments
    )
  }

}

object RepoFactory {

  def build(url: String = "http://"): Repo = {
    new Repo(url = url)
  }

}