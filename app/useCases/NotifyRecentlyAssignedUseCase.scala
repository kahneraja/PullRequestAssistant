package useCases

import domain.GitHub.{Event, PullRequest}
import domain.User
import factories.NotificationMessageFactory
import filters.RecentReviewRequestEventFilter
import gateways.{GitHubGateway, Logger, SlackGateway, TimeProvider}
import repositories.UserRepository

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class NotifyRecentlyAssignedUseCase(
  slackGateway: SlackGateway,
  gitHubGatway: GitHubGateway,
  notificationMessageFactory: NotificationMessageFactory,
  userRepository: UserRepository,
  timeProvider: TimeProvider
) {

  def execute(): Future[List[Future[List[Future[List[Future[Option[User]]]]]]]] = {
    gitHubGatway.getRepos().map { repos =>
      repos.map { repo =>
        gitHubGatway.getPullRequests(s"${repo.url}/pulls").map { pullRequests =>
          pullRequests.map { pullRequest =>
            gitHubGatway.getEvents(pullRequest._links.issue.href).map { events =>
              new RecentReviewRequestEventFilter(timeProvider).filter(events).map { event =>
                notify(event, pullRequest)
              }
            }
          }
        }
      }
    }
  }

  private def notify(event: Event, pullRequest: PullRequest): Future[Option[User]] = {
    userRepository.findUser(event.requested_reviewer.get.login).flatMap {
      case None =>
        Logger.log(s"unable to resolve ${event.review_requester.get.login}")
        Future.successful(None)
      case Some(reviewer) => {
        userRepository.findUser(reviewer.github_name).flatMap {
          case None =>
            Logger.log(s"unable to resolve ${event.review_requester.get.login}")
            Future.successful(None)
          case Some(owner) =>
            val message = notificationMessageFactory.buildRecentlyAssignedMessage(event, pullRequest, owner)
            slackGateway.postMessage(reviewer.slack_name, message)
            Future.successful(Some(reviewer))
        }
      }
    }
  }

}
