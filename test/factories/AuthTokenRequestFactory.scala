package factories

import domain.GitHub.{AuthTokenRequest, Event}

object AuthTokenRequestFactory {
  def build(
    client_id: String = "",
    client_secret: String = "",
    code: String = ""
  ): AuthTokenRequest = {
    new AuthTokenRequest(
      client_id = client_id,
      client_secret = client_secret,
      code = code
    )
  }
}
