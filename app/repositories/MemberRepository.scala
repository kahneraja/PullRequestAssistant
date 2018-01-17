package repositories

import domain.Member
import reactivemongo.api.commands.MultiBulkWriteResult

import scala.concurrent.Future

trait MemberRepository {
  def findMember(githubName: String): Future[Option[Member]]
}
