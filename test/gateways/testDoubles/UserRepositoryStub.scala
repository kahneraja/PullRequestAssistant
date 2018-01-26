package gateways.testDoubles

import domain.User
import factories.UserFactory
import reactivemongo.api.commands.MultiBulkWriteResult
import repositories.UserRepository

import scala.concurrent.Future

object UserRepositoryStub extends UserRepository {
  override def findUser(githubName: String): Future[Option[User]] = {
    Future.successful(Some(UserFactory.build()))
  }
}
