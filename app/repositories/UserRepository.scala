package repositories

import domain.User
import reactivemongo.api.commands.MultiBulkWriteResult

import scala.concurrent.Future

trait UserRepository {
  def findUser(githubName: String): Future[Option[User]]
}
