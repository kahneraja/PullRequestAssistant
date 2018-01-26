package factories

import domain.GitHub.Member

object MemberFactory {

  def build(login: String = "stub-login"): Member = {
    new Member(login = login, "")
  }

}
