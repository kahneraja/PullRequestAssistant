package repositories

import javax.inject.Inject

import domain.GitHub.Metric
import play.api.libs.json.{Json, OWrites}
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.api.Cursor
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

class MetricsRepositoryImpl @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi)
extends MetricsRepository {

  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("metrics"))

  def findAll(): Future[List[Metric]] = {
    collection.flatMap(
      _.find(Json.obj())
        .cursor[Metric]()
        .collect[List](Int.MaxValue, Cursor.FailOnError[List[Metric]]())
    )
  }

  def insert(document: Metric)(implicit writer: OWrites[Metric]): Future[WriteResult] = {
    collection.flatMap(_.insert(document))
  }

  def drop: Future[Boolean] = {
    collection.flatMap(_.drop(false))
  }

}
