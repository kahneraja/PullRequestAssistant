package factories

import domain.GitHub.Team

object TeamFactory {
  def build(name: String = "", url: String = ""): Team = {
    new Team(
      name = name,
      url = url
    )
  }
}
