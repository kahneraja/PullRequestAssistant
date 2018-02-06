package repositories

import javax.inject.Inject

import domain.GitHub.Org
import play.api.libs.json.{Json, OWrites}
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.api.Cursor
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

class OrgRepositoryImpl @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi)
extends OrgRepository {

  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("orgs"))

  def insert(org: Org)(implicit writer: OWrites[Org]): Future[WriteResult] = {
    collection.flatMap(_.insert(org))
  }

  def find(id: Int): Future[Option[Org]] = {
    val selector = BSONDocument("id" -> id)
    collection.flatMap(_.find(selector).one[Org])
  }

  def findByUserId(userId: String): Future[List[Org]] = {
    val selector = BSONDocument("userId" -> userId)

    collection.flatMap(
      _.find(selector)
        .cursor[Org]()
        .collect[List](Int.MaxValue, Cursor.FailOnError[List[Org]]())
    )
  }

}
