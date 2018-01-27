package repositories

import domain.User

import scala.concurrent.Future

trait UserRepository {
  def findUser(githubName: String): Future[Option[User]]
}
