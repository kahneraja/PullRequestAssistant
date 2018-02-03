package repositories

import domain.GitHub.Metric
import play.api.libs.json.OWrites
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

trait MetricRepository {
  def findAll(): Future[List[Metric]]

  def insert(document: Metric)(implicit writer: OWrites[Metric]): Future[WriteResult]

  def drop: Future[Boolean]
}
