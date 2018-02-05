package repositories

import javax.inject.Inject

import domain.User
import play.api.libs.json.OWrites
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.api.commands.{UpdateWriteResult, WriteResult}
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

class UserRepositoryImpl @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi)
  extends UserRepository {

  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("users"))

  def insert(user: User)(implicit writer: OWrites[User]): Future[WriteResult] = {
    collection.flatMap(_.insert(user))
  }

  override def find(userId: String): Future[Option[User]] = {
    val selector = BSONDocument("_id" -> userId)
    collection.flatMap(_.find(selector).one[User])
  }

  def updateSlackToken(user: User, slackToken: String)(implicit writer: OWrites[User]): Future[UpdateWriteResult] = {
    val selector = BSONDocument("_id" -> user._id)
    val updatedUser = user.copy(slackToken = slackToken)
    collection.flatMap(_.update(selector, updatedUser))
  }

}
