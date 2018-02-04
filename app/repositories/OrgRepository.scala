package repositories

import domain.GitHub.Org
import play.api.libs.json.OWrites
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

trait OrgRepository {
  def insert(repo: Org)(implicit writer: OWrites[Org]): Future[WriteResult]
  def find(id: Int): Future[Option[Org]]
}
