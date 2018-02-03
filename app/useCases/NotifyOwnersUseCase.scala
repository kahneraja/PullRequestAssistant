package useCases

import domain.GitHub.PullRequest
import domain.Contributor
import factories.NotificationMessageFactory
import filters.IdlePullRequestFilter
import gateways.{GitHubGateway, Logger, SlackGateway, TimeProvider}
import repositories.ContributorRepository

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class NotifyOwnersUseCase(
  slackGateway: SlackGateway,
  gitHubGatway: GitHubGateway,
  notificationMessageFactory: NotificationMessageFactory,
  contributorRepository: ContributorRepository,
  timeProvider: TimeProvider
) {

  def execute(): Future[List[Future[List[Future[Option[Contributor]]]]]] = {
    gitHubGatway.getRepos().map { repos =>
      repos.map { repo =>
        gitHubGatway.getPullRequests(s"${repo.url}/pulls").map { pullRequests =>
          IdlePullRequestFilter.filter(pullRequests, timeProvider).map { pullRequest =>
            notify(pullRequest)
          }
        }
      }
    }
  }

  private def notify(pullRequest: PullRequest): Future[Option[Contributor]] = {
    contributorRepository.find(pullRequest.user.login).flatMap {
      case None =>
        Logger.log(s"unable to resolve ${pullRequest.user.login}")
        Future.successful(None)
      case Some(owner) =>
        val message = notificationMessageFactory.buildOwnerMessage(pullRequest = pullRequest, owner = owner)
        slackGateway.postMessage(owner.slack_name, message)
        Future.successful(Some(owner))
    }
  }

}
