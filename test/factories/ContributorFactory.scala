package factories

import domain.Contributor

object ContributorFactory {

  def build(github_name: String = "stub-github-name", slack_name: String = "stub-slack-name"): domain.Contributor = {
    new Contributor(github_name = github_name, slack_name = slack_name)
  }

}
