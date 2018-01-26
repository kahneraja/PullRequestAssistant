package factories

import domain.User

object UserFactory {

  def build(github_name: String = "stub-github-name", slack_name: String = "stub-slack-name"): domain.User = {
    new User(github_name = github_name, slack_name = slack_name)
  }

}
