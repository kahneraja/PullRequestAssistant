package repositories

import javax.inject.Inject

import domain.User
import play.api.libs.json.OWrites
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

class UserRepositoryImpl @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi)
  extends UserRepository {

  def collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("users"))

  def insert(user: User)(implicit writer: OWrites[User]): Future[WriteResult] = {
    collection.flatMap(_.insert(user))
  }

}
