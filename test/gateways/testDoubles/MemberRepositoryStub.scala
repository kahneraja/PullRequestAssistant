package gateways.testDoubles

import domain.Member
import factories.MemberFactory
import reactivemongo.api.commands.MultiBulkWriteResult
import repositories.MemberRepository

import scala.concurrent.Future

object MemberRepositoryStub extends MemberRepository {
  override def findMember(githubName: String): Future[Option[Member]] = {
    Future.successful(Some(MemberFactory.build()))
  }
}
