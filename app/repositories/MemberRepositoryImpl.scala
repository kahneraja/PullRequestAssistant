package repositories

import javax.inject.Inject

import domain.User
import play.modules.reactivemongo.json._
import reactivemongo.api.commands.MultiBulkWriteResult
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.bson.{BSONDocument}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

class MemberRepositoryImpl @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi)
  extends MemberRepository {

  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("members"))

  override def findMember(githubName: String): Future[Option[User]] = {
    val selector = BSONDocument("github_name" -> githubName)
    collection.flatMap(_.find(selector).one[User])
  }
}
