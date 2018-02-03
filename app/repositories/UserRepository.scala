package repositories

import domain.User
import play.api.libs.json.OWrites
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

trait UserRepository {
  def insert(user: User)(implicit writer: OWrites[User]): Future[WriteResult]
}
