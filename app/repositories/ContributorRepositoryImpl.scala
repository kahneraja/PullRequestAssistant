package repositories

import javax.inject.Inject

import domain.Contributor
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

class ContributorRepositoryImpl @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi)
  extends ContributorRepository {

  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("members"))

  override def find(githubName: String): Future[Option[Contributor]] = {
    val selector = BSONDocument("github_name" -> githubName)
    collection.flatMap(_.find(selector).one[Contributor])
  }
}
