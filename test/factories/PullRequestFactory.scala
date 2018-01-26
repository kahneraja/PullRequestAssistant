package factories

import java.time.{Clock, LocalDateTime}

import domain.GitHub.{Member, PullRequest, Repo}

object PullRequestFactory {

  def build(
    html_url: String = "http://",
    title: String = "Title",
    created_at: LocalDateTime = LocalDateTime.now(Clock.systemUTC()),
    updated_at: LocalDateTime = LocalDateTime.now(Clock.systemUTC())
  ): PullRequest = {
    new PullRequest(
      title = title,
      html_url = html_url,
      created_at = created_at,
      updated_at = updated_at,
      user = GitHubMemberFactory.build(),
      requested_reviewers = List(GitHubMemberFactory.build())
    )
  }

}

object MemberFactory {

  def build(github_name: String = "stub-github-name", slack_name: String = "stub-slack-name"): domain.User = {
    new domain.User(github_name = github_name, slack_name = slack_name)
  }

}

object GitHubMemberFactory {

  def build(login: String = "stub-login"): Member = {
    new Member(login = login, "")
  }

}

object RepoFactory {

  def build(url: String = "http://"): Repo = {
    new Repo(url = url)
  }

}