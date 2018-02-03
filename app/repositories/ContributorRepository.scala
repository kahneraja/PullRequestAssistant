package repositories

import domain.Contributor

import scala.concurrent.Future

trait ContributorRepository {
  def find(githubName: String): Future[Option[Contributor]]
}
